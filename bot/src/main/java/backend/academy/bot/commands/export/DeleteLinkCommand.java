package backend.academy.bot.commands.export;

import backend.academy.bot.commands.export.ServerCommands;
import backend.academy.dto.BadResponseDTO;
import backend.academy.dto.AddLinkResponseDTO;
import backend.academy.dto.AddLinkRequestDTO;
import backend.academy.dto.DeleteLinkRequestDTO;
import backend.academy.loggs.LoggFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Update;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import static backend.academy.bot.LoggComponent.loggFactory;

/**
 * Класс для удаления ссылки введенной пользователем с сервера.
 */
public class DeleteLinkCommand implements ServerCommands {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String applyCommand(String link, Update update) {
        DeleteLinkRequestDTO deleteLinkRequestDTO = new DeleteLinkRequestDTO();
        deleteLinkRequestDTO.setLink(link);
        String serverUrl = "http://localhost:8081/links";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("tg-chat-id", update.message().chat().id().toString());
        HttpEntity<DeleteLinkRequestDTO> requestEntity = new HttpEntity<>(deleteLinkRequestDTO, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                serverUrl,
                HttpMethod.DELETE,
                requestEntity,
                String.class
            );
            loggFactory.addBotLog("Ответ от сервера: " + response.getStatusCode() + " - " + response.getBody());
            if (response.getStatusCode().is2xxSuccessful()) {
                return "Ссылка успешно удалена.";
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
