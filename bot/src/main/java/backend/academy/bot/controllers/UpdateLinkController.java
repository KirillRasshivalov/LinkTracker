package backend.academy.bot.controllers;

import static backend.academy.bot.MyTelegramBot.notificationMessage;

import backend.academy.bot.services.BotLogger;
import backend.academy.dto.LinkUpdateRequestDTO;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Контроллер для принятия ссылок которые были обновлены и юзеров которые ее отслеживали. */
@RestController
@SuppressFBWarnings("SPRING_CSRF_UNRESTRICTED_REQUEST_MAPPING")
public class UpdateLinkController {

    @PostMapping
    @RequestMapping("/updates")
    public ResponseEntity<?> notifyUser(@RequestBody LinkUpdateRequestDTO linkUpdateRequestDTO) {
        try {
            notificationMessage(linkUpdateRequestDTO.getTgChatIds(), linkUpdateRequestDTO.getDescription());
            BotLogger.LOGGER
                    .atInfo()
                    .setMessage(linkUpdateRequestDTO.getDescription())
                    .log();

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            BotLogger.LOGGER.atInfo().setMessage(e.getMessage()).log();

            return ResponseEntity.badRequest().build();
        }
    }
}
