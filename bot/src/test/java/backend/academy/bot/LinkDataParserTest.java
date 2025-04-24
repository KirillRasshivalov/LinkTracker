// package backend.academy.bot;
//
// import static org.junit.jupiter.api.Assertions.assertTrue;
//
// import backend.academy.bot.managers.links.LinksDataParser;
// import backend.academy.dto.LinkInfoDTO;
// import backend.academy.dto.ShowListResponseDTO;
// import java.util.List;
// import org.junit.jupiter.api.Test;
//
// public class LinkDataParserTest {
//
//    @Test
//    public void testOfCorrectLinkParsing() {
//        ShowListResponseDTO showListResponseDTO = new ShowListResponseDTO();
//        LinkInfoDTO linkInfoDTO = new LinkInfoDTO(12L, "http:/test", List.of("tag1", "tag2"), List.of("filt1"));
//        showListResponseDTO.setLinks(List.of(linkInfoDTO));
//        showListResponseDTO.setSize(2L);
//        assertTrue(LinksDataParser.parseInfo(showListResponseDTO).contains("http:/test"));
//        assertTrue(LinksDataParser.parseInfo(showListResponseDTO).contains("[tag1, tag2]"));
//        assertTrue(LinksDataParser.parseInfo(showListResponseDTO).contains("[filt1]"));
//    }
// }
