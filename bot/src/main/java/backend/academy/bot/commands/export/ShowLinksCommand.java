package backend.academy.bot.commands.export;

import backend.academy.bot.managers.links.LinksDataParser;
import backend.academy.dto.BadResponseDTO;
import backend.academy.dto.ShowListResponseDTO;
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
 * Класс для показа всех ссылок которые принадлежат данному пользователю.
 */
public class ShowLinksCommand implements ServerCommands{

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String applyCommand(String message, Update update) {

        loggFactory.addBotLog("Команда на показ всех доступнух пользователю ссылок " + message);

        String serverUrl = "http://localhost:8081/links";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("tg-chat-id", update.message().chat().id().toString());
        HttpEntity<?> requestEntity = new HttpEntity<>(null, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                serverUrl,
                HttpMethod.GET,
                requestEntity,
                String.class
            );
            loggFactory.addBotLog("Ответ от сервера: " + response.getStatusCode() + " - " + response.getBody());
            if (response.getStatusCode().is2xxSuccessful()) {
                ShowListResponseDTO showListResponseDTO = objectMapper.readValue(response.getBody(), ShowListResponseDTO.class);
                return LinksDataParser.parseInfo(showListResponseDTO);
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
