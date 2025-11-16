package backend.academy.scrapper.controllers;

import backend.academy.dto.ShowListResponseDTO;
import backend.academy.scrapper.managers.ErrorHandler;
import backend.academy.scrapper.services.DatabaseService;
import backend.academy.scrapper.services.LinkService;
import backend.academy.scrapper.services.ServerLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/** Контроллер для вывода всех отслеживаемых ссылок данным пользователем. */
@RestController
@RequiredArgsConstructor
public class ShowLinkController {

    private final DatabaseService databaseService;
    private final LinkService linkService;

    @GetMapping("/links")
    public ResponseEntity<?> showLink(@RequestHeader("tg-chat-id") String chatId) {
        ServerLogger.LOGGER
                .atInfo()
                .setMessage("Пришел запрос на показ действующий ссылок " + chatId)
                .log();

        Long id = Long.parseLong(chatId);

        if (linkService.findConnectedLinks(id)) {
            ShowListResponseDTO showListResponseDTO = new ShowListResponseDTO();
            showListResponseDTO.setLinks(databaseService.showLinks(id));
            showListResponseDTO.setSize((long) databaseService.showLinks(id).size());
            return ResponseEntity.ok().body(showListResponseDTO);
        }
        return ResponseEntity.badRequest().body(ErrorHandler.userHasNoLinks());
    }
}
