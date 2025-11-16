package backend.academy.scrapper.notifications;

import backend.academy.dto.LinkUpdateRequestDTO;
import backend.academy.dto.MainInfoFromGithubDTO;
import backend.academy.dto.MainInfoFromStackOverlowDTO;
import backend.academy.scrapper.managers.CheckLink;
import backend.academy.scrapper.services.ServerLogger;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/** Реализация метода отправки ответа по средствам http протокола. */
public class HTTPSender implements Sender {
    private final CheckLink checkLink = new CheckLink();

    @Override
    public void sendNotification(String link, List<Long> IDS, Object info) {
        if (checkLink.isGitHubLink(link)) {
            MainInfoFromGithubDTO mainInfoFromGithubDTO = (MainInfoFromGithubDTO) info;
            for (int i = 0; i < IDS.size(); i++) {
                LinkUpdateRequestDTO linkUpdateRequestDTO = new LinkUpdateRequestDTO();
                RestTemplate restTemplate = new RestTemplate();
                linkUpdateRequestDTO.setUrl(link);
                linkUpdateRequestDTO.setId(IDS.get(i));
                linkUpdateRequestDTO.setTgChatIds(IDS);
                linkUpdateRequestDTO.setDescription("Пришло обновление по ссылке: " + link + ".\n" + "Автор: "
                        + mainInfoFromGithubDTO.authorName() + ".\n" + "Содержание: "
                        + mainInfoFromGithubDTO.message() + ".\n" + "Название " + mainInfoFromGithubDTO.type() + ": "
                        + mainInfoFromGithubDTO.nameOfAnswer() + ".\n" + "Время последнего коммита: "
                        + mainInfoFromGithubDTO.createdAt() + ".\n");
                String botUrl = "http://localhost:8080/updates";
                HttpEntity<LinkUpdateRequestDTO> httpEntity = new HttpEntity<>(linkUpdateRequestDTO);

                ResponseEntity<String> response =
                        restTemplate.exchange(botUrl, HttpMethod.POST, httpEntity, String.class);
                ServerLogger.LOGGER
                        .atInfo()
                        .setMessage("Получен ответ:" + response.getBody())
                        .log();
            }
        } else {
            MainInfoFromStackOverlowDTO mainInfoFromStackOverlowDTO = (MainInfoFromStackOverlowDTO) info;
            for (int i = 0; i < IDS.size(); i++) {
                LinkUpdateRequestDTO linkUpdateRequestDTO = new LinkUpdateRequestDTO();
                RestTemplate restTemplate = new RestTemplate();
                linkUpdateRequestDTO.setUrl(link);
                linkUpdateRequestDTO.setId(IDS.get(i));
                linkUpdateRequestDTO.setTgChatIds(IDS);
                linkUpdateRequestDTO.setDescription("Пришло обновление по ссылке: " + link + ".\n" + "Автор: "
                        + mainInfoFromStackOverlowDTO.name() + ".\n" + "Тема: "
                        + mainInfoFromStackOverlowDTO.theme() + ".\n" + "Комментарий: "
                        + mainInfoFromStackOverlowDTO.answer() + ".\n" + "Время последнего коммита: "
                        + mainInfoFromStackOverlowDTO.time() + ".\n");
                String botUrl = "http://localhost:8080/updates";
                HttpEntity<LinkUpdateRequestDTO> httpEntity = new HttpEntity<>(linkUpdateRequestDTO);

                ResponseEntity<String> response =
                        restTemplate.exchange(botUrl, HttpMethod.POST, httpEntity, String.class);
                ServerLogger.LOGGER
                        .atInfo()
                        .setMessage("Получен ответ:" + response.getBody())
                        .log();
            }
        }
    }
}
