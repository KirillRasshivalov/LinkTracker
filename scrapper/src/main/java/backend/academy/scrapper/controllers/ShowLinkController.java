package backend.academy.scrapper.controllers;

import static backend.academy.scrapper.components.LogComponent.loggFactory;

import backend.academy.dto.LinkInfoDTO;
import backend.academy.dto.ShowListResponseDTO;
import backend.academy.scrapper.data.LinkData;
import backend.academy.scrapper.managers.Collection;
import backend.academy.scrapper.managers.ErrorHandler;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/** Контроллер для вывода всех отслеживаемых ссылок данным пользователем. */
@RestController
public class ShowLinkController {

    @GetMapping("/links")
    public ResponseEntity<?> showLink(@RequestHeader("tg-chat-id") String chatId) {
        loggFactory.addServerLog("Пришел запрос на показ действующий ссылок " + chatId);

        Long id = Long.parseLong(chatId);

        if (Collection.idInfo.containsKey(id)) {
            ShowListResponseDTO showListResponseDTO = new ShowListResponseDTO();
            List<LinkData> linkInfoDTOS = Collection.idInfo.get(id);
            List<LinkInfoDTO> linkInfoDTOList = new ArrayList<>();
            for (LinkData linkData : linkInfoDTOS) {
                linkInfoDTOList.add(new LinkInfoDTO(id, linkData.link(), linkData.tags(), linkData.filter()));
            }
            showListResponseDTO.setLinks(linkInfoDTOList);
            showListResponseDTO.setSize((long) linkInfoDTOList.size());

            return ResponseEntity.ok().body(showListResponseDTO);
        } else {
            return ResponseEntity.badRequest().body(ErrorHandler.userHasNoLinks());
        }
    }
}
