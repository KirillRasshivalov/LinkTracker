package backend.academy.scrapper.servises;

import backend.academy.dto.LinkUpdateRequestDTO;
import backend.academy.scrapper.managers.Collection;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/** Сервис для проверки ссылок на обновления, и последующей отправки их юзерам. */
@Service
public class UpdateCheckerService {

    private final Map<String, Instant> TRACKED_LINKS = new HashMap<>();
    private final Map<String, List<Long>> LINKS = Collection.linksOwners;
    private final GitHubService GIT_HUB_SERVICE;
    private final StackOverflowService STACKOVERFLOW_SERVICE;

    public UpdateCheckerService(GitHubService gitHubService, StackOverflowService stackOverflowService) {
        this.GIT_HUB_SERVICE = gitHubService;
        this.STACKOVERFLOW_SERVICE = stackOverflowService;
    }

    private boolean isGitHubLink(String link) {
        return link.startsWith("https://github.com/");
    }

    private boolean isStackOverflowLink(String link) {
        return link.startsWith("https://stackoverflow.com/");
    }

    @Scheduled(fixedRate = 300000)
    public void checkForUpdates() {
        for (Map.Entry<String, List<Long>> entry : LINKS.entrySet()) {
            String link = entry.getKey();
            if (isGitHubLink(link)) {
                GIT_HUB_SERVICE.getLastCommitDate(link).subscribe(lastCommitDate -> {
                    Instant lastUpdated = TRACKED_LINKS.get(link);
                    if (lastUpdated == null || lastCommitDate.isAfter(lastUpdated)) {
                        TRACKED_LINKS.put(link, lastCommitDate);
                        notifyUser(link);
                    }
                });
            } else if (isStackOverflowLink(link)) {
                STACKOVERFLOW_SERVICE.getLastActivityDate(link).subscribe(lastActivityDate -> {
                    Instant lastUpdated = TRACKED_LINKS.get(link);
                    if (lastUpdated == null || lastActivityDate.isAfter(lastUpdated)) {
                        TRACKED_LINKS.put(link, lastActivityDate);
                        notifyUser(link);
                    }
                });
            }
        }
    }

    private void notifyUser(String link) {

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
    }
}
