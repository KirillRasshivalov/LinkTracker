package backend.academy.bot.commands.export;

import backend.academy.bot.managers.links.LinksDataParser;
import backend.academy.bot.services.BotLogger;
import backend.academy.dto.BadResponseDTO;
import backend.academy.dto.ShowListResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Update;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/** Класс для показа всех ссылок которые принадлежат данному пользователю. */
@Component
public class ShowLinksCommand implements ServerCommands {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Cacheable(value = "list_cache", key = "#update.message().chat().id().toString()")
    public String applyCommand(String message, Update update) {

        BotLogger.LOGGER
                .atInfo()
                .addArgument("Команда на показ всех доступнух пользователю ссылок " + message)
                .log();

        String serverUrl = "http://localhost:8081/links";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("tg-chat-id", update.message().chat().id().toString());
        HttpEntity<?> requestEntity = new HttpEntity<>(null, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.exchange(serverUrl, HttpMethod.GET, requestEntity, String.class);
            BotLogger.LOGGER
                    .atInfo()
                    .setMessage("Ответ от сервера: " + response.getStatusCode() + " - " + response.getBody())
                    .log();
            if (response.getStatusCode().is2xxSuccessful()) {
                ShowListResponseDTO showListResponseDTO =
                        objectMapper.readValue(response.getBody(), ShowListResponseDTO.class);
                return LinksDataParser.parseInfo(showListResponseDTO);
            } else {
                return "Ошибка при показе доступных команд: " + response.getStatusCode();
            }
        } catch (HttpClientErrorException e) {
            BotLogger.LOGGER
                    .atError()
                    .setMessage("Ошибка 400 при показе команд: " + e.getResponseBodyAsString())
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
