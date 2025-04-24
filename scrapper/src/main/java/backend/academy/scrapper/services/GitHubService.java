package backend.academy.scrapper.services;

import backend.academy.dto.GitHubIssueResponseDTO;
import backend.academy.dto.MainInfoFromGithubDTO;
import backend.academy.scrapper.ScrapperConfig;
import backend.academy.scrapper.managers.GitHubLinkParser;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/** Класс для инициализыции и отправки запросов на обновление на гитхаб. */
@Service
public class GitHubService {

    private final WebClient WEB_CLIENT;
    private final String GIT_HUB_TOKEN;

    public GitHubService(WebClient.Builder webClientBuilder, ScrapperConfig appConfig) {
        this.WEB_CLIENT = webClientBuilder.baseUrl("https://api.github.com").build();
        this.GIT_HUB_TOKEN = appConfig.githubToken();
    }

    public Mono<MainInfoFromGithubDTO> getInfoFromIssue(String url) {
        GitHubLinkParser.GitHubLink link = GitHubLinkParser.parse(url);
        return WEB_CLIENT
                .get()
                .uri("/repos/{owner}/{repo}/issues?state=all", link.getOwner(), link.getRepo())
                .header("Authorization", "Bearer " + GIT_HUB_TOKEN)
                .retrieve()
                .bodyToFlux(GitHubIssueResponseDTO.class)
                .take(1)
                .map(issue -> new MainInfoFromGithubDTO(
                        issue.getBody(),
                        issue.getUser() != null ? issue.getUser().getLogin() : "unknown",
                        issue.getCreated_at(),
                        issue.getTitle()))
                .singleOrEmpty();
    }
}
