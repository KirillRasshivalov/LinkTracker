package backend.academy.scrapper.controllers;

import static backend.academy.scrapper.components.LogComponent.loggFactory;

import backend.academy.scrapper.managers.Collection;
import backend.academy.scrapper.managers.ErrorHandler;
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
        loggFactory.addServerLog("Пришел запрос на регистрацию пользователя " + id);

        if (Collection.activeUsers.contains(Long.valueOf(id))) {
            return ResponseEntity.badRequest().body(ErrorHandler.chatHasAlreadyExist());
        } else {
            Collection.activeUsers.add(Long.valueOf(id));
            return ResponseEntity.ok().build();
        }
    }
}
