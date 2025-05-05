 package backend.academy.bot;

 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertNotEquals;
 import static org.mockito.Mockito.when;

 import backend.academy.bot.commands.CommandHandler;
 import com.pengrad.telegrambot.TelegramBot;
 import com.pengrad.telegrambot.model.Chat;
 import com.pengrad.telegrambot.model.Message;
 import com.pengrad.telegrambot.model.Update;
 import java.io.IOException;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 import org.mockito.Mock;
 import org.mockito.MockitoAnnotations;

 public class UnknownCommandTest {
    @Mock
    private TelegramBot bot;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    private MyTelegramBot myTelegramBot;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        myTelegramBot = new MyTelegramBot(new BotConfig("test"));
        myTelegramBot.init();
    }

    @Test
    void testHandleUpdate_UnknownCommand() {
        long chatId = 12345L;
        String unknownCommand = "/unknown";

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(unknownCommand);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);

        assertEquals("Данная команда не поддерживается.", CommandHandler.getCommandMessage(update));
    }

    @Test
    void testHandleUpdate_KnownCommand() {
        long chatId = 12345L;
        String unknownCommand = "/list";

        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(unknownCommand);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatId);

        assertNotEquals("Данная команда не поддерживается.", CommandHandler.getCommandMessage(update));
    }
 }
