package backend.academy.scrapper.kafka;

import backend.academy.dto.LinkUpdateRequestDTO;
import backend.academy.scrapper.services.ServerLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/** Продюсер для отправки сообщений в очередь. */
@Service
@RequiredArgsConstructor
public class Producer {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(LinkUpdateRequestDTO message, boolean isError) throws JsonProcessingException {
        ServerLogger.LOGGER.atInfo().setMessage("Отправил сообщение в брокер.").log();
        String topic = isError ? "error-messages" : "valid-messages";
        String jsonMessage = objectMapper.writeValueAsString(message);
        kafkaTemplate.send(topic, String.valueOf(message.getId()), jsonMessage);
    }
}
