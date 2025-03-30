package backend.academy.scrapper.services;

import backend.academy.scrapper.notifications.HTTPSender;
import backend.academy.scrapper.managers.Collection;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

    @Scheduled(fixedRate = 10000)
    public void checkForUpdates() {
        HTTPSender httpSender = new HTTPSender();
        for (Map.Entry<String, List<Long>> entry : LINKS.entrySet()) {
            String link = entry.getKey();
            if (isGitHubLink(link)) {
                ServerLogger.LOGGER
                        .atInfo()
                        .setMessage("Проверяем наличие обновлений по ссылки с гитхаба.")
                        .log();
                GIT_HUB_SERVICE
                        .getLastCommitDate(link)
                        .subscribe(
                                lastCommitDate -> {
                                    Instant lastUpdated = null;
                                    if (TRACKED_LINKS.containsKey(link)) {
                                        lastUpdated = TRACKED_LINKS.get(link);
                                    }
                                    if (lastUpdated == null || lastCommitDate.isAfter(lastUpdated)) {
                                        TRACKED_LINKS.put(link, lastCommitDate);
                                        httpSender.sendNotification(link, LINKS);
                                    }
                                },
                                error -> ServerLogger.LOGGER
                                        .atError()
                                        .setMessage("Ошибка при проверке обновлений для ссылки: " + link + ", ошибка: "
                                                + error.getMessage())
                                        .log());
            } else if (isStackOverflowLink(link)) {
                ServerLogger.LOGGER
                        .atInfo()
                        .setMessage("Проверяем наличие обновлений по ссылке с стековерфлоу.")
                        .log();
                STACKOVERFLOW_SERVICE
                        .getLastActivityDate(link)
                        .subscribe(
                                lastActivityDate -> {
                                    Instant lastUpdated = null;
                                    if (TRACKED_LINKS.containsKey(link)) {
                                        lastUpdated = TRACKED_LINKS.get(link);
                                    }
                                    if (lastUpdated == null || lastActivityDate.isAfter(lastUpdated)) {
                                        TRACKED_LINKS.put(link, lastActivityDate);
                                        httpSender.sendNotification(link, LINKS);
                                    }
                                },
                                error -> ServerLogger.LOGGER
                                        .atError()
                                        .setMessage("Ошибка при проверке обновлений для ссылки: " + link + ", ошибка: "
                                                + error.getMessage())
                                        .log());
            }
        }
    }
}
