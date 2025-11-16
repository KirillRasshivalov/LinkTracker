// package backend.academy.scrapper;
//
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
// import backend.academy.dto.DeleteLinkRequestDTO;
// import backend.academy.scrapper.controllers.DeleteLinkController;
// import backend.academy.scrapper.data.LinkData;
// import backend.academy.scrapper.managers.Collection;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.List;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
//
// @WebMvcTest(DeleteLinkController.class)
// public class DeleteLinkControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        Collection.idInfo = new HashMap<>();
//        Collection.linksOwners = new HashMap<>();
//        Collection.activeUsers = new HashSet<>();
//    }
//
//    @AfterEach
//    void tearDown() {
//        Collection.idInfo.clear();
//        Collection.linksOwners.clear();
//        Collection.activeUsers.clear();
//    }
//
//    @Test
//    void testDeleteLink_Success() throws Exception {
//        DeleteLinkRequestDTO deleteLinkRequestDTO = new DeleteLinkRequestDTO();
//        deleteLinkRequestDTO.setLink("https://www.google.com");
//
//        String link = "https://www.google.com";
//
//        Collection.linksOwners.put(link, new ArrayList<>(List.of(12345L)));
//        LinkData linkData = new LinkData(link, null, null);
//        ArrayList<LinkData> linkDataList = new ArrayList<>();
//        linkDataList.add(linkData);
//        Collection.idInfo.put(12345L, linkDataList);
//
//        String chatId = "12345";
//
//        mockMvc.perform(delete("/links")
//                        .header("tg-chat-id", chatId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(deleteLinkRequestDTO)))
//                .andExpect(status().isOk());
//
//        Long id = Long.valueOf(chatId);
//        assertTrue(!Collection.linksOwners.get(link).contains(id), "Ссылка не удалена из коллекции");
//    }
// }
