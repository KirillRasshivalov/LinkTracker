package backend.academy.scrapper.controllers;

import backend.academy.dto.DeleteLinkRequestDTO;
import backend.academy.dto.DeleteLinkResponceDTO;
import backend.academy.scrapper.managers.ErrorHandler;
import backend.academy.scrapper.services.DatabaseService;
import backend.academy.scrapper.services.LinkService;
import backend.academy.scrapper.services.ServerLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/** Контроллер на удаление ссылок. */
@RestController
@RequiredArgsConstructor
public class DeleteLinkController {

    private final DatabaseService databaseService;
    private final LinkService linkService;

    @DeleteMapping("/links")
    public ResponseEntity<?> deleteLink(
            @RequestBody DeleteLinkRequestDTO link, @RequestHeader("tg-chat-id") String chatId) {
        ServerLogger.LOGGER
                .atInfo()
                .setMessage("Пришел запрос на удаление ссылки " + chatId)
                .log();

        Long id = Long.valueOf(chatId);

        if (linkService.findLink(link.getLink(), id)) {
            databaseService.deleteLink(id, link.getLink());
            DeleteLinkResponceDTO deleteLinkResponceDTO = new DeleteLinkResponceDTO();
            deleteLinkResponceDTO.setId(id);
            deleteLinkResponceDTO.setUrl(link.getLink());

            return ResponseEntity.ok().body(deleteLinkResponceDTO);
        }
        return ResponseEntity.badRequest().body(ErrorHandler.linkDoesntExist());
    }
}
