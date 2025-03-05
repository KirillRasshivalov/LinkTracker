package backend.academy.scrapper.servises;

import backend.academy.dto.StackOverflowResponseDTO;
import backend.academy.scrapper.ScrapperConfig;
import backend.academy.scrapper.managers.StackOverflowLinkParser;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.Instant;

/**
 * Класс для инициализации и отправки запросов на проверку обновления ссылок на стековерфлоу.
 */
@Service
public class StackOverflowService {

    private final WebClient webClient;
    private final String stackoverflowKey;
    private final String stackoverflowAccessToken;

    public StackOverflowService(WebClient.Builder webClientBuilder, ScrapperConfig appConfig) {
        this.webClient = webClientBuilder.baseUrl("https://api.stackexchange.com/2.3").build();
        this.stackoverflowKey = appConfig.stackOverflow().key();
        this.stackoverflowAccessToken = appConfig.stackOverflow().accessToken();
    }

    public Mono<Instant> getLastActivityDate(String url) {
        String questionId = StackOverflowLinkParser.parseQuestionId(url);
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/questions/{id}")
                .queryParam("site", "stackoverflow")
                .queryParam("key", stackoverflowKey)
                .queryParam("access_token", stackoverflowAccessToken)
                .build(questionId))
            .retrieve()
            .bodyToMono(StackOverflowResponseDTO.class)
            .map(response -> {
                if (!response.getItems().isEmpty()) {
                    return Instant.ofEpochSecond(response.getItems().get(0).getCreation_date());
                }
                return null;
            });
    }
}
