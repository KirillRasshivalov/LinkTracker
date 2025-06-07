package backend.academy.bot.commands.export;

import backend.academy.bot.services.BotLogger;
import backend.academy.dto.BadResponseDTO;
import backend.academy.dto.DeleteLinkRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Update;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/** Класс для удаления ссылки введенной пользователем с сервера. */
@Component
public class DeleteLinkCommand implements ServerCommands {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    @CacheEvict(value = "list_cache", key="#update.message().chat().id().toString()")
    public String applyCommand(String link, Update update) {

        DeleteLinkRequestDTO deleteLinkRequestDTO = new DeleteLinkRequestDTO();
        deleteLinkRequestDTO.setLink(link);

        String serverUrl = "http://localhost:8081/links";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("tg-chat-id", update.message().chat().id().toString());
        HttpEntity<DeleteLinkRequestDTO> requestEntity = new HttpEntity<>(deleteLinkRequestDTO, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.exchange(serverUrl, HttpMethod.DELETE, requestEntity, String.class);
            BotLogger.LOGGER
                    .atInfo()
                    .setMessage("Ответ от сервера: " + response.getStatusCode() + " - " + response.getBody())
                    .log();
            if (response.getStatusCode().is2xxSuccessful()) {
                return "Ссылка успешно удалена.";
            } else {
                return "Ошибка при удаление ссылки: " + response.getStatusCode();
            }
        } catch (HttpClientErrorException e) {
            BotLogger.LOGGER
                    .atError()
                    .setMessage("Ошибка 400 при удаление ссылки: " + e.getResponseBodyAsString())
                    .log();
            try {
                BadResponseDTO errorResponse =
                        OBJECT_MAPPER.readValue(e.getResponseBodyAsString(), BadResponseDTO.class);

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
