package backend.academy.scrapper.servises;

import backend.academy.dto.GitHubResponseDTO;
import backend.academy.scrapper.ScrapperConfig;
import backend.academy.scrapper.managers.GitHubLinkParser;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.Instant;

/**
 * Класс для инициализыции и отправки запросов на обновление на гитхаб.
 */
@Service
public class GitHubService {

    private final WebClient webClient;
    private final String githubToken;

    public GitHubService(WebClient.Builder webClientBuilder, ScrapperConfig appConfig) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
        this.githubToken = appConfig.githubToken();
    }

    public Mono<Instant> getLastCommitDate(String url) {
        GitHubLinkParser.GitHubLink link = GitHubLinkParser.parse(url);
        return webClient.get()
            .uri("/repos/{owner}/{repo}/commits", link.getOwner(), link.getRepo())
            .header("Authorization", "Bearer " + githubToken)
            .retrieve()
            .bodyToFlux(GitHubResponseDTO.class)
            .take(1)
            .map(commit -> commit.getCommit().getDate())
            .singleOrEmpty();
    }
}
