package backend.academy.scrapper.controllers;

import backend.academy.dto.AddLinkRequestDTO;
import backend.academy.scrapper.ErrorHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AddLinkController {

    private List<String> links = new ArrayList<>();


    @PostMapping("/links")
    public ResponseEntity<?> updateCollection(
        @RequestBody AddLinkRequestDTO requestDTO,
        @RequestHeader("tg-chat-id") String chatId
    ) {
        if (requestDTO.getLink() != null) {
            links.add(requestDTO.getLink());
            AddLinkRequestDTO responseDTO = new AddLinkRequestDTO();
            responseDTO.setLink(requestDTO.getLink());
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.badRequest().body(ErrorHandler.sameLinkError());
        }
    }
}
