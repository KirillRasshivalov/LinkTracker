package backend.academy.scrapper.controllers;

import backend.academy.scrapper.managers.ErrorHandler;
import backend.academy.scrapper.services.DatabaseService;
import backend.academy.scrapper.services.ServerLogger;
import backend.academy.scrapper.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Контроллер для удаления неактивного пользователя. */
@RestController
@RequestMapping("/tg-chat")
@RequiredArgsConstructor
public class DeleteUserController {

    private final DatabaseService databaseService;
    private final UserService userService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        ServerLogger.LOGGER
                .atInfo()
                .setMessage("Пришел запрос на eдаление пользователя " + id)
                .log();

        if (!userService.findUser(Long.parseLong(id))) {
            ServerLogger.LOGGER
                    .atError()
                    .setMessage("Пользователя не существует.")
                    .log();
            return ResponseEntity.badRequest().body(ErrorHandler.userNotExist());
        }
        databaseService.deleteUser(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }
}
