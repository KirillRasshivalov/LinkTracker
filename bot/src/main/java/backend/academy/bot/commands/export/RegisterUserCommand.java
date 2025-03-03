package backend.academy.bot.commands.export;

import backend.academy.dto.BadResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import static backend.academy.bot.LoggComponent.loggFactory;


/**
 * Класс для регистрации пользователя на сервере и добавление его в активные пользователи.
 */
public class RegisterUserCommand implements ServerCommands {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String applyCommand(String command, Update update) {
        loggFactory.addBotLog("Команда на добавление пользователя " + command);
        String serverUrl = "http://localhost:8081/tg-chat/{id}";
        String id = update.message().chat().id().toString();
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                serverUrl,
                null,
                String.class,
                id
            );
            loggFactory.addBotLog("Ответ от сервера: " + response.getStatusCode() + " - " + response.getBody());
            if (response.getStatusCode().is2xxSuccessful()) {
                return "Чат зарегистрирован.";
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
