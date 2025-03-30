package backend.academy.scrapper.notifications;

import backend.academy.dto.LinkUpdateRequestDTO;
import backend.academy.scrapper.services.ServerLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HTTPSender implements Sender {

    @Override
    public void sendNotification(String link, Map<String, List<Long>> LINKS) {
        LinkUpdateRequestDTO linkUpdateRequestDTO = new LinkUpdateRequestDTO();
        RestTemplate restTemplate = new RestTemplate();
        linkUpdateRequestDTO.setUrl(link);
        linkUpdateRequestDTO.setId(LINKS.get(link).get(0));
        List<Long> chatsId = new ArrayList<>();

        for (int i = 0; i < LINKS.get(link).size(); i++) {
            chatsId.add(LINKS.get(link).get(i));
        }

        linkUpdateRequestDTO.setTgChatIds(chatsId);
        linkUpdateRequestDTO.setDescription("Пришло обновление по ссылке: " + link);
        String botUrl = "http://localhost:8080/updates";
        HttpEntity<LinkUpdateRequestDTO> httpEntity = new HttpEntity<>(linkUpdateRequestDTO);

        ResponseEntity<String> response = restTemplate.exchange(botUrl, HttpMethod.POST, httpEntity, String.class);
        ServerLogger.LOGGER
            .atInfo()
            .setMessage("Получен ответ:" + response.getBody())
            .log();
    }
}
