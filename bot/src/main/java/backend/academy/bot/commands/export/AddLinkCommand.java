package backend.academy.bot.commands.export;

import backend.academy.bot.services.BotLogger;
import backend.academy.dto.AddLinkRequestDTO;
import backend.academy.dto.BadResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.Update;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/** Класс для отправки новой отслеживаемой ссылки на скрепер и ожидающий ответа от него. */
@SuppressWarnings("StringSplitter")
public class AddLinkCommand implements ServerCommands {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private AddLinkRequestDTO lastRequestDTO;

    @Override
    @CacheEvict(value="list_cach")
    public String applyCommand(@NotNull String command, @NotNull Update update) {

        BotLogger.LOGGER
                .atInfo()
                .setMessage("Команда на добавление ссылки " + command)
                .log();
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
            BotLogger.LOGGER
                    .atInfo()
                    .setMessage("Ответ от сервера: " + response.getStatusCode() + " - " + response.getBody())
                    .log();
            if (response.getStatusCode().is2xxSuccessful()) {
                return "Ссылка успешно добавлена.";
            } else {
                return "Ошибка в добавление ссылки: " + response.getStatusCode();
            }
        } catch (HttpClientErrorException e) {
            BotLogger.LOGGER
                    .atError()
                    .setMessage("Ошибка 400 при добавлении ссылки: " + e.getResponseBodyAsString())
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

    public AddLinkRequestDTO getLastRequestDTO() {
        return lastRequestDTO;
    }
}
