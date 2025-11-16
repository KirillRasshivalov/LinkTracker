package backend.academy.scrapper.controllers;

import backend.academy.dto.AddLinkRequestDTO;
import backend.academy.dto.AddLinkResponseDTO;
import backend.academy.scrapper.data.LinkData;
import backend.academy.scrapper.managers.ErrorHandler;
import backend.academy.scrapper.services.DatabaseService;
import backend.academy.scrapper.services.LinkService;
import backend.academy.scrapper.services.ServerLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/** Контроллер на добавление отслеживаемых ссылок. */
@RestController
@RequiredArgsConstructor
public class AddLinkController {

    private final DatabaseService databaseService;
    private final LinkService linkService;

    @PostMapping("/links")
    public ResponseEntity<?> updateCollection(
            @RequestBody AddLinkRequestDTO requestDTO, @RequestHeader("tg-chat-id") String chatId) {

        ServerLogger.LOGGER
                .atInfo()
                .setMessage("Пришел запрос на добавление ссылки от " + chatId)
                .log();

        LinkData linkData = new LinkData(requestDTO.getLink(), requestDTO.getFilters(), requestDTO.getTags());
        Long id = Long.valueOf(chatId);

        if (linkService.findLink(linkData.link(), id)) {
            return ResponseEntity.badRequest().body(ErrorHandler.sameLinkError());
        }
        databaseService.addLink(
                id,
                linkData.link(),
                linkData.filter().toString(),
                linkData.tags().toString());
        AddLinkResponseDTO responseDTO = new AddLinkResponseDTO();
        responseDTO.setId(chatId);
        responseDTO.setUrl(requestDTO.getLink());
        responseDTO.setFilters(requestDTO.getFilters());
        responseDTO.setTags(requestDTO.getTags());

        return ResponseEntity.ok(responseDTO);
    }
}
