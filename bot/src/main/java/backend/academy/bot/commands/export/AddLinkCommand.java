package backend.academy.bot.commands.export;

import backend.academy.dto.AddLinkResponseDTO;
import backend.academy.dto.BadResponseDTO;
import backend.academy.dto.AddLinkRequestDTO;
import backend.academy.loggs.LoggFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Update;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
        loggFactory.addLog("Команда на добавление ссылки " + command);
        AddLinkRequestDTO collectionUpdateRequestDTO = new AddLinkRequestDTO();

        String[] parts = command.split(" < ");
        String link = parts[0].trim();
        List<String> tags = Arrays.stream(parts[1].trim().split(" "))
            .collect(Collectors.toList());
        List<String> filters = Arrays.stream(parts[2].trim().split(" "))
            .collect(Collectors.toList());

        collectionUpdateRequestDTO.setLink(link);
        collectionUpdateRequestDTO.setTags(tags);
        collectionUpdateRequestDTO.setFilters(filters);

        String serverUrl = "http://localhost:8081/links";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("tg-chat-id", update.message().chat().id().toString());
        HttpEntity<AddLinkRequestDTO> requestEntity = new HttpEntity<>(collectionUpdateRequestDTO, headers);

        try {
            ResponseEntity<?> response = restTemplate.postForEntity(
                serverUrl,
                requestEntity,
                String.class
            );
            if (response.hasBody()) {
                if (response.getStatusCode().is2xxSuccessful()) {
                    AddLinkResponseDTO collectionUpdateResponseDTO = objectMapper.readValue(
                        response.getBody().toString(),
                        AddLinkResponseDTO.class
                    );
                    return "Ссылка " + collectionUpdateResponseDTO.getUrl() + " успешно добавлена.";
                } else if (response.getStatusCode().is4xxClientError()) {
                    BadResponseDTO badResponseDTO = objectMapper.readValue(
                        response.getBody().toString(),
                        BadResponseDTO.class
                    );
                    return badResponseDTO.getExceptionName();
                } else {
                    LoggFactory.addLog("Неопознаная ошибка" + response.getStatusCode());
                    return "Что то пошло не так.";
                }
            }
        } catch (Exception e) {
            LoggFactory.addLog(e.getMessage());
        }
        return null;
    }
}
