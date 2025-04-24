//package backend.academy.scrapper;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import backend.academy.dto.AddLinkRequestDTO;
//import backend.academy.dto.AddLinkResponseDTO;
//import backend.academy.scrapper.data.LinkData;
//import backend.academy.scrapper.managers.Collection;
//import backend.academy.scrapper.managers.ErrorHandler;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class DoubleLinkTests {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        Collection.idInfo.clear();
//        Collection.linksOwners.clear();
//        Collection.activeUsers.clear();
//    }
//
//    @Test
//    void testAddLink_LinkAlreadyExists() throws Exception {
//        Long chatId = 12345L;
//        String link = "https://example.com";
//        List<String> tags = List.of("tag1", "tag2");
//        List<String> filters = List.of("filter1", "filter2");
//
//        LinkData existingLinkData = new LinkData(link, filters, tags);
//        Collection.idInfo.put(chatId, new ArrayList<>());
//        Collection.idInfo.get(chatId).add(existingLinkData);
//
//        AddLinkRequestDTO requestDTO = new AddLinkRequestDTO();
//        requestDTO.setLink(link);
//        requestDTO.setTags(tags);
//        requestDTO.setFilters(filters);
//
//        mockMvc.perform(post("/links")
//                        .header("tg-chat-id", chatId.toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.description")
//                        .value(ErrorHandler.sameLinkError().getDescription()));
//    }
//
//    @Test
//    void testAddLink_LinkDoesntExists() throws Exception {
//        Long chatId = 12345L;
//        String link = "https://example.com";
//        List<String> tags = List.of("tag1", "tag2");
//        List<String> filters = List.of("filter1", "filter2");
//
//        AddLinkResponseDTO responseDTO = new AddLinkResponseDTO();
//        responseDTO.setTags(tags);
//        responseDTO.setFilters(filters);
//        responseDTO.setUrl(link);
//        responseDTO.setId(chatId.toString());
//
//        AddLinkRequestDTO requestDTO = new AddLinkRequestDTO();
//        requestDTO.setLink(link);
//        requestDTO.setTags(tags);
//        requestDTO.setFilters(filters);
//
//        mockMvc.perform(post("/links")
//                        .header("tg-chat-id", chatId.toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isOk());
//    }
//}
