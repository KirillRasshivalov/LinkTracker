package backend.academy.bot.controllers;

import backend.academy.dto.AddLinkRequestDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @PostMapping
    @RequestMapping("/updates")
    public AddLinkRequestDTO print() {
        return new AddLinkRequestDTO();
    }
}
