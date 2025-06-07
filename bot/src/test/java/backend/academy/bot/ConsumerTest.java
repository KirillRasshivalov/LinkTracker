package backend.academy.bot;

import backend.academy.bot.kafka.Consumer;
import backend.academy.bot.services.BotLogger;
import backend.academy.dto.LinkUpdateRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static backend.academy.bot.MyTelegramBot.notificationMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = "valid-messages")
@DirtiesContext
public class ConsumerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private Consumer consumer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        consumer = new Consumer();
    }

    @Test
    void testHandleValidMessage_Success() throws JsonProcessingException {
        LinkUpdateRequestDTO request = new LinkUpdateRequestDTO();
        request.setTgChatIds(List.of(123L, 456L));
        request.setDescription("Test message");
        String jsonMessage = objectMapper.writeValueAsString(request);
        kafkaTemplate.send("valid-messages", jsonMessage);

        assertEquals(request.getTgChatIds(), List.of(123L, 456L));
        assertEquals(request.getDescription(), "Test message");
    }
}
