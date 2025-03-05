package backend.academy.bot.controllers;

import backend.academy.dto.LinkUpdateRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static backend.academy.bot.MyTelegramBot.notificationMessage;

/**
 * Контроллер для принятия ссылок которые были обновлены и юзеров которые ее отслеживали.
 */
@RestController
public class UpdateLinkController {

    @PostMapping
    @RequestMapping("/updates")
    public ResponseEntity<?> notifyUser(@RequestBody LinkUpdateRequestDTO linkUpdateRequestDTO) {
        try {
            notificationMessage(linkUpdateRequestDTO.getTgChatIds(), linkUpdateRequestDTO.getDescription());

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().build();
        }
    }
}
