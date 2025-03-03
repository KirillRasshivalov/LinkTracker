package backend.academy.bot.commands.export;

import backend.academy.bot.services.ServerData;
import backend.academy.dto.AddLinkResponseDTO;
import backend.academy.dto.BadResponseDTO;
import backend.academy.dto.AddLinkRequestDTO;
import backend.academy.loggs.LoggFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Update;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static backend.academy.bot.LoggComponent.loggFactory;

/**
 * Класс для отправки новой отслеживаемой ссылки на скрепер и ожидающий ответа от него.
 */
public class AddLinkCommand implements ServerCommands {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String applyCommand(@NotNull String command, @NotNull Update update) {
        loggFactory.addBotLog("Команда на добавление ссылки " + command);
        AddLinkRequestDTO collectionUpdateRequestDTO = new AddLinkRequestDTO();
        String serverUrl = "http://localhost:8081/links";

        String[] parts = command.split(" < ");
        String link = parts[0].trim();
        List<String> tags = Arrays.stream(parts[1].trim().split(" "))
            .collect(Collectors.toList());
        List<String> filters = Arrays.stream(parts[2].trim().split(" "))
            .collect(Collectors.toList());

        collectionUpdateRequestDTO.setLink(link);
        collectionUpdateRequestDTO.setTags(tags);
        collectionUpdateRequestDTO.setFilters(filters);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("tg-chat-id", update.message().chat().id().toString());
        HttpEntity<AddLinkRequestDTO> requestEntity = new HttpEntity<>(collectionUpdateRequestDTO, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                serverUrl,
                requestEntity,
                String.class
            );
            loggFactory.addBotLog("Ответ от сервера: " + response.getStatusCode() + " - " + response.getBody());
            if (response.getStatusCode().is2xxSuccessful()) {
                return "Ссылка успешно добавлена.";
            } else {
                return "Ошибка: " + response.getStatusCode();
            }
        } catch (HttpClientErrorException e) {
            loggFactory.addBotLog("Ошибка 400: " + e.getResponseBodyAsString());
            try {
                BadResponseDTO errorResponse = objectMapper.readValue(e.getResponseBodyAsString(), BadResponseDTO.class);
                return errorResponse.getDescription();
            } catch (Exception jsonException) {
                loggFactory.addBotLog("Ошибка при разборе JSON ответа: " + jsonException.getMessage());
                return "Ошибка 400, но не удалось разобрать ответ.";
            }
        } catch (Exception e) {
            loggFactory.addBotLog("Неизвестная ошибка: " + e);
            return "Что-то пошло не так.";
        }
    }
}
