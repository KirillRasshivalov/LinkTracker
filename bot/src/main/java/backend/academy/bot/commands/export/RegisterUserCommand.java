package backend.academy.bot.commands.export;

import backend.academy.bot.services.BotLogger;
import backend.academy.dto.BadResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/** Класс для регистрации пользователя на сервере и добавление его в активные пользователи. */
public class RegisterUserCommand implements ServerCommands {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String applyCommand(String command, Update update) {

        BotLogger.LOGGER
                .atInfo()
                .setMessage("Команда на добавление пользователя " + command)
                .log();

        String serverUrl = "http://localhost:8081/tg-chat/{id}";
        String id = update.message().chat().id().toString();
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, null, String.class, id);
            BotLogger.LOGGER
                    .atInfo()
                    .setMessage("Ответ от сервера: " + response.getStatusCode() + " - " + response.getBody())
                    .log();
            if (response.getStatusCode().is2xxSuccessful()) {
                return "Чат зарегистрирован.";
            } else {
                return "Ошибка при регистрации пользователя: " + response.getStatusCode();
            }
        } catch (HttpClientErrorException e) {
            BotLogger.LOGGER
                    .atError()
                    .setMessage("Ошибка 400 при регистрации пользователя: " + e.getResponseBodyAsString())
                    .log();
            try {
                BadResponseDTO errorResponse =
                        objectMapper.readValue(e.getResponseBodyAsString(), BadResponseDTO.class);

                return errorResponse.getDescription();
            } catch (Exception jsonException) {
                BotLogger.LOGGER
                        .atError()
                        .setMessage("Ошибка при разборе JSON ответа: " + jsonException.getMessage())
                        .log();

                return "Ошибка 400, но не удалось разобрать ответ.";
            }
        } catch (Exception e) {
            BotLogger.LOGGER.atError().setMessage("Неизвестная ошибка: " + e).log();

            return "Что-то пошло не так.";
        }
    }
}
