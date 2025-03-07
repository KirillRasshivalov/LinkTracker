package backend.academy.scrapper;

import backend.academy.dto.AddLinkRequestDTO;
import backend.academy.scrapper.controllers.AddLinkController;
import backend.academy.scrapper.data.LinkData;
import backend.academy.scrapper.managers.Collection;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddLinkController.class)
public class AddLinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Collection.idInfo = new HashMap<>();
        Collection.linksOwners = new HashMap<>();
        Collection.activeUsers = new HashSet<>();
    }

    @AfterEach
    void tearDown() {
        Collection.idInfo.clear();
        Collection.linksOwners.clear();
        Collection.activeUsers.clear();
    }

    @Test
    void testAddLink_Success() throws Exception {
        AddLinkRequestDTO requestDTO = new AddLinkRequestDTO();
        requestDTO.setLink("https://example.com");
        requestDTO.setFilters(new ArrayList<>());
        requestDTO.setTags(new ArrayList<>());

        String chatId = "12345";

        mockMvc.perform(post("/links")
                .header("tg-chat-id", chatId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
            .andExpect(status().isOk());

        Long id = Long.valueOf(chatId);
        LinkData expectedLinkData = new LinkData(requestDTO.getLink(), requestDTO.getFilters(), requestDTO.getTags());

        assertTrue(Collection.idInfo.containsKey(id), "Пользователь не добавлен в коллекцию");
        assertTrue(Collection.idInfo.get(id).contains(expectedLinkData), "Ссылка не добавлена в коллекцию");
    }
}
