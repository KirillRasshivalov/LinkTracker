package backend.academy.scrapper.servises;

import backend.academy.dto.GitHubResponseDTO;
import backend.academy.scrapper.ScrapperConfig;
import backend.academy.scrapper.managers.GitHubLinkParser;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Класс для инициализыции и отправки запросов на обновление на гитхаб.
 */
@Service
public class GitHubService {

    private final WebClient WEB_CLIENT;
    private final String GIT_HUB_TOKEN;

    public GitHubService(WebClient.Builder webClientBuilder, ScrapperConfig appConfig) {
        this.WEB_CLIENT = webClientBuilder.baseUrl("https://api.github.com").build();
        this.GIT_HUB_TOKEN = appConfig.githubToken();
    }

    public Mono<Instant> getLastCommitDate(String url) {
        GitHubLinkParser.GitHubLink link = GitHubLinkParser.parse(url);
        return WEB_CLIENT.get()
            .uri("/repos/{owner}/{repo}/commits", link.getOwner(), link.getRepo())
            .header("Authorization", "Bearer " + GIT_HUB_TOKEN)
            .retrieve()
            .bodyToFlux(GitHubResponseDTO.class)
            .take(1)
            .map(commit -> commit.getCommit().getDate())
            .singleOrEmpty();
    }
}
