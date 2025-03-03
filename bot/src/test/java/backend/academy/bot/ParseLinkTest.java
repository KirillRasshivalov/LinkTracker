package backend.academy.bot;

import backend.academy.bot.commands.export.AddLinkCommand;
import backend.academy.dto.AddLinkRequestDTO;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class ParseLinkTest {

    private AddLinkCommand addLinkCommand;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addLinkCommand = new AddLinkCommand();

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(12345L);
    }

    @Test
    void testApplyCommandParsingCorrect() {
        String command = "https://example.com < tag1 tag2 < filter1 filter2";

        String result = addLinkCommand.applyCommand(command, update);

        AddLinkRequestDTO requestDTO = addLinkCommand.getLastRequestDTO();

        assertNotNull(requestDTO);
        assertEquals("https://example.com", requestDTO.getLink());
        assertEquals(List.of("tag1", "tag2"), requestDTO.getTags());
        assertEquals(List.of("filter1", "filter2"), requestDTO.getFilters());
    }

    @Test
    void testApplyCommandWithoutTagAndFilter() {
        String command = "https://example.com <  <  ";

        String result = addLinkCommand.applyCommand(command, update);

        AddLinkRequestDTO requestDTO = addLinkCommand.getLastRequestDTO();

        assertNotNull(requestDTO);
        assertEquals("https://example.com", requestDTO.getLink());
        assertEquals(List.of(""), requestDTO.getTags());
        assertEquals(List.of(""), requestDTO.getFilters());
    }
}
