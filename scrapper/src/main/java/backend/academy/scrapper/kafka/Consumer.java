package backend.academy.scrapper.kafka;

import backend.academy.dto.LinkUpdateRequestDTO;
import backend.academy.scrapper.services.ServerLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/** Класс консьюмер для принятия сообщений с брокера. */
@Service
public class Consumer {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @KafkaListener(topics = "valid-messages", groupId = "json-consumer-groupp")
    public void handleValidMessage(String jsonMessage) {
        ServerLogger.LOGGER.atInfo().setMessage("Сообщение пришло в консьюмер").log();
        try {
            RestTemplate restTemplate = new RestTemplate();
            LinkUpdateRequestDTO message = objectMapper.readValue(jsonMessage, LinkUpdateRequestDTO.class);
            String botUrl = "http://localhost:8080/updates";
            HttpEntity<LinkUpdateRequestDTO> httpEntity = new HttpEntity<>(message);
            ResponseEntity<String> response = restTemplate.exchange(botUrl, HttpMethod.POST, httpEntity, String.class);
            ServerLogger.LOGGER
                    .atInfo()
                    .setMessage("Пришел ответ: " + response.getBody())
                    .log();
        } catch (JsonProcessingException e) {
            ServerLogger.LOGGER.atError().setMessage(e.getMessage()).log();
        }
    }
}
