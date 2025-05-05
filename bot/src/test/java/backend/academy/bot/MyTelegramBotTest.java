 package backend.academy.bot;

 import static org.junit.jupiter.api.Assertions.assertEquals;
 import static org.mockito.Mockito.*;
 import static org.mockito.Mockito.times;
 import static org.mockito.Mockito.verify;

 import com.pengrad.telegrambot.TelegramBot;
 import com.pengrad.telegrambot.request.SendMessage;
 import java.util.Arrays;
 import java.util.List;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 import org.mockito.ArgumentCaptor;
 import org.mockito.Mockito;

 class MyTelegramBotTest {

    private TelegramBot mockBot;
    private MyTelegramBot myTelegramBot;

    @BeforeEach
    void setUp() {
        mockBot = Mockito.mock(TelegramBot.class);
        setStaticBotField(mockBot);
        myTelegramBot = new MyTelegramBot(new BotConfig("rrr"));
    }

    @Test
    void testNotificationMessageSendsOnlyToSpecifiedIds() {
        List<Long> userIds = Arrays.asList(123L, 456L);
        List<Long> extraIds = Arrays.asList(789L, 999L);
        String message = "Test message";
        MyTelegramBot.notificationMessage(userIds, message);
        ArgumentCaptor<SendMessage> sendMessageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(mockBot, times(2)).execute(sendMessageCaptor.capture());
        List<SendMessage> sentMessages = sendMessageCaptor.getAllValues();
        assertEquals(123L, sentMessages.get(0).getParameters().get("chat_id"));
        assertEquals(456L, sentMessages.get(1).getParameters().get("chat_id"));

        for (Long extraId : extraIds) {
            verify(mockBot, never()).execute(new SendMessage(extraId, message));
        }
    }

    private void setStaticBotField(TelegramBot mockBot) {
        try {
            var field = MyTelegramBot.class.getDeclaredField("bot");
            field.setAccessible(true);
            field.set(null, mockBot);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка", e);
        }
    }
 }
