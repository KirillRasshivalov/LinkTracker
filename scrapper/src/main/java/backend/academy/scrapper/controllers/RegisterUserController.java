package backend.academy.scrapper.controllers;

import backend.academy.scrapper.managers.Collection;
import backend.academy.scrapper.managers.ErrorHandler;
import backend.academy.scrapper.services.DatabaseService;
import backend.academy.scrapper.services.ServerLogger;
import backend.academy.scrapper.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Контроллер который регистрирует новых пользователей. */
@RestController
@RequestMapping("/tg-chat")
@RequiredArgsConstructor
public class RegisterUserController {

    private final UserService userService;
    private final DatabaseService databaseService;

    @PostMapping("/{id}")
    public ResponseEntity<?> addActiveUser(@PathVariable String id) {
        ServerLogger.LOGGER
                .atInfo()
                .setMessage("Пришел запрос на регистрацию пользователя " + id)
                .log();

        if (userService.findUser(Long.valueOf(id))) {
            return ResponseEntity.badRequest().body(ErrorHandler.chatHasAlreadyExist());
        }
        databaseService.addUser(Long.valueOf(id));
        Collection.activeUsers.add(Long.valueOf(id));
        return ResponseEntity.ok().build();
    }
}
