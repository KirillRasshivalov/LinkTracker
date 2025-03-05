package backend.academy.bot.commands.export;

import backend.academy.dto.BadResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Update;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import static backend.academy.bot.LoggComponent.loggFactory;

/**
 * Класс для удаления юзера из активных на сервере.
 */
public class DeleteUserCommand implements ServerCommands {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String applyCommand(String command, Update update) {

        loggFactory.addBotLog("Команда на удаления пользователя " + command);

        String serverUrl = "http://localhost:8081/links";
        RestTemplate restTemplate = new RestTemplate();
        String id = update.message().chat().id().toString();

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                serverUrl,
                HttpMethod.DELETE,
                null,
                String.class,
                id
            );
            loggFactory.addBotLog("Ответ от сервера: " + response.getStatusCode() + " - " + response.getBody());
            if (response.getStatusCode().is2xxSuccessful()) {
                return "Чат успешно удален.";
            } else {
                return "Ошибка: " + response.getStatusCode();
            }
        } catch (HttpClientErrorException e) {
            loggFactory.addBotLog("Ошибка 400: " + e.getResponseBodyAsString());
            try {
                BadResponseDTO errorResponse = OBJECT_MAPPER.readValue(
                    e.getResponseBodyAsString(),
                    BadResponseDTO.class
                );

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
