package backend.academy.bot.commands.kafka;

import static backend.academy.bot.MyTelegramBot.notificationMessage;

import backend.academy.bot.services.BotLogger;
import backend.academy.dto.LinkUpdateRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/** Класс консьюмер для принятия сообщений с брокера. */
@Service
public class Consumer {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @KafkaListener(topics = "valid-messages", groupId = "json-consumer-groupp")
    public void handleValidMessage(String jsonMessage) {
        BotLogger.LOGGER.atInfo().setMessage("Сообщение пришло в консьюмер").log();
        try {
            LinkUpdateRequestDTO message = objectMapper.readValue(jsonMessage, LinkUpdateRequestDTO.class);
            notificationMessage(message.getTgChatIds(), message.getDescription());
            BotLogger.LOGGER
                    .atInfo()
                    .setMessage("Пришел ответ: " + message.toString())
                    .log();
        } catch (JsonProcessingException e) {
            BotLogger.LOGGER.atError().setMessage(e.getMessage()).log();
        }
    }
}
