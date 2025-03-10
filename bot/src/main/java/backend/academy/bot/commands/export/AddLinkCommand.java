package backend.academy.bot.commands.export;

import static backend.academy.bot.LoggComponent.loggFactory;

import backend.academy.dto.AddLinkRequestDTO;
import backend.academy.dto.BadResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Update;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/** Класс для отправки новой отслеживаемой ссылки на скрепер и ожидающий ответа от него. */
public class AddLinkCommand implements ServerCommands {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private AddLinkRequestDTO lastRequestDTO;

    @Override
    public String applyCommand(@NotNull String command, @NotNull Update update) {

        loggFactory.addBotLog("Команда на добавление ссылки " + command);
        AddLinkRequestDTO collectionUpdateRequestDTO = new AddLinkRequestDTO();
        String serverUrl = "http://localhost:8081/links";

        String[] parts = command.split(" < ");
        String link = parts[0].trim();
        List<String> tags = Arrays.stream(parts[1].trim().split(" ")).collect(Collectors.toList());
        List<String> filters = Arrays.stream(parts[2].trim().split(" ")).collect(Collectors.toList());

        collectionUpdateRequestDTO.setLink(link);
        collectionUpdateRequestDTO.setTags(tags);
        collectionUpdateRequestDTO.setFilters(filters);
        this.lastRequestDTO = collectionUpdateRequestDTO;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("tg-chat-id", update.message().chat().id().toString());
        HttpEntity<AddLinkRequestDTO> requestEntity = new HttpEntity<>(collectionUpdateRequestDTO, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
            loggFactory.addBotLog("Ответ от сервера: " + response.getStatusCode() + " - " + response.getBody());
            if (response.getStatusCode().is2xxSuccessful()) {
                return "Ссылка успешно добавлена.";
            } else {
                return "Ошибка: " + response.getStatusCode();
            }
        } catch (HttpClientErrorException e) {
            loggFactory.addBotLog("Ошибка 400: " + e.getResponseBodyAsString());
            try {
                BadResponseDTO errorResponse =
                        OBJECT_MAPPER.readValue(e.getResponseBodyAsString(), BadResponseDTO.class);

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

    public AddLinkRequestDTO getLastRequestDTO() {
        return lastRequestDTO;
    }
}
