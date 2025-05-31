package backend.academy.scrapper.kafka;

import backend.academy.dto.LinkUpdateRequestDTO;
import backend.academy.scrapper.services.ServerLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Класс консьюмер для принятия сообщений с брокера.
 */
@Service
public class Consumer {
    private static final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());

    @KafkaListener(topics = "valid-messages")
    public void handleValidMessage(String jsonMessage) {
        try {
            LinkUpdateRequestDTO message = objectMapper.readValue(jsonMessage, LinkUpdateRequestDTO.class);
        } catch (JsonProcessingException e) {
            ServerLogger.LOGGER.atError().setMessage(e.getMessage());
        }
    }
}
