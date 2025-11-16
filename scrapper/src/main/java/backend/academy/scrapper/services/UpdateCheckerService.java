package backend.academy.scrapper.services;

import backend.academy.scrapper.managers.CheckLink;
import backend.academy.scrapper.models.LinkInfo;
import backend.academy.scrapper.notifications.HTTPSender;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/** Сервис для проверки ссылок на обновления, и последующей отправки их юзерам. */
@Service
@SuppressFBWarnings("JLM_JSR166_UTILCONCURRENT_MONITORENTER")
public class UpdateCheckerService {

    private final GitHubService GIT_HUB_SERVICE;
    private final StackOverflowService STACKOVERFLOW_SERVICE;
    private final DatabaseService DATABASE_SERVICE;
    private final CheckLink checkLink = new CheckLink();

    private final Map<String, Instant> trackedLinks = new ConcurrentHashMap<>();

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public UpdateCheckerService(
            GitHubService gitHubService, StackOverflowService stackOverflowService, DatabaseService databaseService) {
        this.GIT_HUB_SERVICE = gitHubService;
        this.STACKOVERFLOW_SERVICE = stackOverflowService;
        this.DATABASE_SERVICE = databaseService;
    }

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void checkForUpdates() {
        HTTPSender httpSender = new HTTPSender();
        int pageSize = 100;
        int pageNumber = 0;

        Page<LinkInfo> page;

        do {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            page = DATABASE_SERVICE.showLinks(pageable);

            for (LinkInfo linkInfo : page.getContent()) {
                var unused = executor.submit(() -> processLink(linkInfo, httpSender));
            }
            pageNumber++;
        } while (page.hasNext());
    }

    private void processLink(LinkInfo linkInfo, HTTPSender httpSender) {
        String url = linkInfo.link();
        try {
            if (checkLink.isGitHubLink(url)) {
                ServerLogger.LOGGER
                        .atInfo()
                        .setMessage("Проверяем наличие обновлений по ссылки с гитхаба.")
                        .log();
                synchronized (GIT_HUB_SERVICE) {
                    GIT_HUB_SERVICE
                            .getInfoFromIssue(url)
                            .subscribe(
                                    issue -> {
                                        Instant lastUpdated = trackedLinks.get(url);
                                        if (lastUpdated == null
                                                || issue.createdAt().isAfter(lastUpdated)) {

                                            synchronized (trackedLinks) {
                                                trackedLinks.put(url, issue.createdAt());
                                            }
                                            httpSender.sendNotification(url, extractUserIds(linkInfo), issue);
                                        }
                                    },
                                    error -> ServerLogger.LOGGER
                                            .atError()
                                            .setMessage("Ошибка при проверке обновлений для ссылки: " + url
                                                    + ", ошибка: " + error.getMessage())
                                            .log());
                    GIT_HUB_SERVICE
                            .getInfoFromPullRequest(url)
                            .subscribe(
                                    pullRequest -> {
                                        Instant lastUpdated = trackedLinks.get(url);
                                        if (lastUpdated == null
                                                || pullRequest.createdAt().isAfter(lastUpdated)) {
                                            synchronized (trackedLinks) {
                                                trackedLinks.put(url, pullRequest.createdAt());
                                            }
                                            httpSender.sendNotification(url, extractUserIds(linkInfo), pullRequest);
                                        }
                                    },
                                    error -> ServerLogger.LOGGER
                                            .atError()
                                            .setMessage("Ошибка при проверке обновлений для пул реквеста ссылки: " + url
                                                    + "\nОшибка " + error.getMessage())
                                            .log());
                }
            } else if (checkLink.isStackOverflowLink(url)) {
                ServerLogger.LOGGER
                        .atInfo()
                        .setMessage("Проверяем наличие обновлений по ссылке с стековерфлоу.")
                        .log();
                synchronized (STACKOVERFLOW_SERVICE) {
                    STACKOVERFLOW_SERVICE
                            .getInfoFromStackOverflow(url)
                            .subscribe(
                                    info -> {
                                        Instant lastUpdated = trackedLinks.get(url);
                                        if (lastUpdated == null || info.time().isAfter(lastUpdated)) {
                                            synchronized (trackedLinks) {
                                                trackedLinks.put(url, info.time());
                                            }
                                            httpSender.sendNotification(url, extractUserIds(linkInfo), info);
                                        }
                                    },
                                    error -> ServerLogger.LOGGER
                                            .atError()
                                            .setMessage("Ошибка при проверке обновлений для ссылки: " + url
                                                    + ", ошибка: " + error.getMessage())
                                            .log());
                }
            }
        } catch (Exception ex) {
            ServerLogger.LOGGER
                    .atError()
                    .setMessage("Ошибка при проверке ссылки: " + url + ": " + ex.getMessage())
                    .log();
        }
    }

    private List<Long> extractUserIds(LinkInfo linkInfo) {
        return linkInfo.users().stream().map(u -> u.userId()).toList();
    }
}
