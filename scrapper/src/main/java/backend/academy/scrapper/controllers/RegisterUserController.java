package backend.academy.scrapper.controllers;

import backend.academy.scrapper.managers.Collection;
import backend.academy.scrapper.managers.ErrorHandler;
import backend.academy.scrapper.services.ServerLogger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Контроллер который регистрирует новых пользователей. */
@RestController
@RequestMapping("/tg-chat")
public class RegisterUserController {

    @PostMapping("/{id}")
    public ResponseEntity<?> addActiveUser(@PathVariable String id) {
        ServerLogger.LOGGER
                .atInfo()
                .setMessage("Пришел запрос на регистрацию пользователя " + id)
                .log();

        if (Collection.activeUsers.contains(Long.valueOf(id))) {
            return ResponseEntity.badRequest().body(ErrorHandler.chatHasAlreadyExist());
        } else {
            Collection.activeUsers.add(Long.valueOf(id));
            return ResponseEntity.ok().build();
        }
    }
}
