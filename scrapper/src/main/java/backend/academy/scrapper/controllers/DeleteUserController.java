package backend.academy.scrapper.controllers;

import backend.academy.scrapper.managers.Collection;
import backend.academy.scrapper.managers.ErrorHandler;
import backend.academy.scrapper.services.ServerLogger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Контроллер для удаления неактивного пользователя. */
@RestController
@RequestMapping("/tg-chat")
public class DeleteUserController {

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        ServerLogger.LOGGER
                .atInfo()
                .setMessage("Пришел запрос на eдаление пользователя " + id)
                .log();

        if (!Collection.activeUsers.contains(Long.valueOf(id))) {
            return ResponseEntity.badRequest().body(ErrorHandler.userNotExist());
        } else {
            return ResponseEntity.ok().build();
        }
    }
}
