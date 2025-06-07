package backend.academy.scrapper;

import backend.academy.dto.LinkUpdateRequestDTO;
import backend.academy.scrapper.kafka.Producer;
import backend.academy.scrapper.services.ServerLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ServerLogger serverLogger;

    @Captor
    private ArgumentCaptor<String> messageCaptor;

    private Producer producer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        producer = new Producer(kafkaTemplate);
    }

    @Test
    void sendMessage_ValidDto_CorrectJsonSent() throws JsonProcessingException {
        LinkUpdateRequestDTO request = new LinkUpdateRequestDTO();
        request.setId(1L);
        request.setTgChatIds(List.of(123L, 456L));
        request.setDescription("Test update");
        request.setUrl("https://example.com");

        producer.sendMessage(request, false);

        verify(kafkaTemplate).send(eq("valid-messages"), eq("1"), messageCaptor.capture());

        String sentJson = messageCaptor.getValue();
        LinkUpdateRequestDTO deserialized = objectMapper.readValue(sentJson, LinkUpdateRequestDTO.class);

        assertEquals(request.getId(), deserialized.getId());
        assertEquals(request.getTgChatIds(), deserialized.getTgChatIds());
        assertEquals(request.getDescription(), deserialized.getDescription());
        assertEquals(request.getUrl(), deserialized.getUrl());

        verify(serverLogger).LOGGER.atInfo().setMessage("Отправил сообщение в брокер.").log();
    }
}
