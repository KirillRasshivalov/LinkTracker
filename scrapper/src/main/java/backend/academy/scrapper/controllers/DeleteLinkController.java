package backend.academy.scrapper.controllers;

import backend.academy.dto.DeleteLinkRequestDTO;
import backend.academy.dto.DeleteLinkResponceDTO;
import backend.academy.scrapper.managers.Collection;
import backend.academy.scrapper.managers.ErrorHandler;
import backend.academy.scrapper.data.LinkData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import static backend.academy.scrapper.components.LogComponent.loggFactory;

/**
 * Контроллер на удаление ссылок.
 */
@RestController
public class DeleteLinkController {

    @DeleteMapping("/links")
    public ResponseEntity<?> deleteLink(
        @RequestBody DeleteLinkRequestDTO link,
        @RequestHeader("tg-chat-id") String chatId
    ) {
        loggFactory.addServerLog("Пришел запрос на удаление ссылки " + chatId);
        Long id = Long.valueOf(chatId);
        if (Collection.idInfo.containsKey(id)) {
            List<LinkData> listToRemove = new ArrayList<>();
            List<LinkData> currUserList = Collection.idInfo.get(id);
            for (LinkData linkData : currUserList) {
                System.out.println(linkData.link() + " " + link.getLink());
                if (linkData.link().equals(link.getLink())) {
                    listToRemove.add(linkData);
                    Collection.linksOwners.get(link.getLink()).remove(id);
                }
            }
            if (listToRemove.isEmpty()) {
                return ResponseEntity.badRequest().body(ErrorHandler.linkDoesntExist());
            }
            for (LinkData linkData : listToRemove) {
                Collection.idInfo.get(id).remove(linkData);
                if (Collection.idInfo.get(id).isEmpty()) Collection.idInfo.remove(id);
            }
            DeleteLinkResponceDTO deleteLinkResponceDTO = new DeleteLinkResponceDTO();
            deleteLinkResponceDTO.setId(id);
            deleteLinkResponceDTO.setUrl(link.getLink());
            return ResponseEntity.ok().body(deleteLinkResponceDTO);
        } else {
            return ResponseEntity.badRequest().body(ErrorHandler.linkDoesntExist());
        }
    }
}
