package backend.academy.scrapper.servises;

import backend.academy.dto.StackOverflowResponseDTO;
import backend.academy.scrapper.ScrapperConfig;
import backend.academy.scrapper.managers.StackOverflowLinkParser;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/** Класс для инициализации и отправки запросов на проверку обновления ссылок на стековерфлоу. */
@Service
public class StackOverflowService {

    private final WebClient WEB_CLIENT;
    private final String STACKOVERFLOW_KEY;
    private final String STACKOVERFLOW_ACCESS_TOKEN;

    public StackOverflowService(WebClient.Builder webClientBuilder, ScrapperConfig appConfig) {
        this.WEB_CLIENT =
                webClientBuilder.baseUrl("https://api.stackexchange.com/2.3").build();
        this.STACKOVERFLOW_KEY = appConfig.stackOverflow().key();
        this.STACKOVERFLOW_ACCESS_TOKEN = appConfig.stackOverflow().accessToken();
    }

    public Mono<Instant> getLastActivityDate(String url) {
        String questionId = StackOverflowLinkParser.parseQuestionId(url);
        return WEB_CLIENT
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/questions/{id}")
                        .queryParam("site", "stackoverflow")
                        .queryParam("key", STACKOVERFLOW_KEY)
                        .queryParam("access_token", STACKOVERFLOW_ACCESS_TOKEN)
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
