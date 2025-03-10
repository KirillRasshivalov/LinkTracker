package backend.academy.scrapper.controllers;

import static backend.academy.scrapper.components.LogComponent.loggFactory;

import backend.academy.dto.AddLinkRequestDTO;
import backend.academy.dto.AddLinkResponseDTO;
import backend.academy.scrapper.data.LinkData;
import backend.academy.scrapper.managers.Collection;
import backend.academy.scrapper.managers.ErrorHandler;
import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/** Контроллер на добавление отслеживаемых ссылок. */
@RestController
public class AddLinkController {

    @PostMapping("/links")
    public ResponseEntity<?> updateCollection(
            @RequestBody AddLinkRequestDTO requestDTO, @RequestHeader("tg-chat-id") String chatId) {

        loggFactory.addServerLog("Пришел запрос на добавление ссылки от " + chatId);

        LinkData linkData = new LinkData(requestDTO.getLink(), requestDTO.getFilters(), requestDTO.getTags());
        Long id = Long.valueOf(chatId);
        Collection.activeUsers.add(id);

        if (!Collection.idInfo.containsKey(id) || !Collection.idInfo.get(id).contains(linkData)) {
            if (Collection.idInfo.containsKey(id)) {
                Collection.idInfo.get(Long.valueOf(chatId)).add(linkData);
            } else {
                Collection.idInfo.put(Long.valueOf(chatId), new ArrayList<>());
                Collection.idInfo.get(Long.valueOf(chatId)).add(linkData);
            }
            if (Collection.linksOwners.containsKey(requestDTO.getLink())) {
                Collection.linksOwners.get(requestDTO.getLink()).add(id);
            } else {
                Collection.linksOwners.put(requestDTO.getLink(), new ArrayList<>());
                Collection.linksOwners.get(requestDTO.getLink()).add(id);
            }
            AddLinkResponseDTO responseDTO = new AddLinkResponseDTO();
            responseDTO.setId(chatId);
            responseDTO.setUrl(requestDTO.getLink());
            responseDTO.setFilters(requestDTO.getFilters());
            responseDTO.setTags(requestDTO.getTags());

            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.badRequest().body(ErrorHandler.sameLinkError());
        }
    }
}
