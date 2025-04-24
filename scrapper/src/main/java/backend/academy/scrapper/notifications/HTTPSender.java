package backend.academy.scrapper.notifications;

import backend.academy.dto.LinkUpdateRequestDTO;
import backend.academy.dto.MainInfoFromGithubDTO;
import backend.academy.scrapper.services.ServerLogger;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/** Реализация метода отправки ответа по средствам http протокола. */
public class HTTPSender implements Sender {

    @Override
    public void sendNotification(String link, List<Long> IDS, MainInfoFromGithubDTO info) {

        for (int i = 0; i < IDS.size(); i++) {
            LinkUpdateRequestDTO linkUpdateRequestDTO = new LinkUpdateRequestDTO();
            RestTemplate restTemplate = new RestTemplate();
            linkUpdateRequestDTO.setUrl(link);
            linkUpdateRequestDTO.setId(IDS.get(i));
            linkUpdateRequestDTO.setTgChatIds(IDS);
            linkUpdateRequestDTO.setDescription("Пришло обновление по ссылке: " + link + ".\n" + "Автор: "
                    + info.authorName() + ".\n" + "Содержание: "
                    + info.message() + ".\n" + "Название issue: "
                    + info.nameOfIssue() + ".\n" + "Время последнего коммита: "
                    + info.createdAt() + ".\n");
            String botUrl = "http://localhost:8080/updates";
            HttpEntity<LinkUpdateRequestDTO> httpEntity = new HttpEntity<>(linkUpdateRequestDTO);

            ResponseEntity<String> response = restTemplate.exchange(botUrl, HttpMethod.POST, httpEntity, String.class);
            ServerLogger.LOGGER
                    .atInfo()
                    .setMessage("Получен ответ:" + response.getBody())
                    .log();
        }
    }
}
