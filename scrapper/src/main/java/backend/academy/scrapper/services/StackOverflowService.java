package backend.academy.scrapper.services;

import backend.academy.dto.MainInfoFromStackOverlowDTO;
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

    public Mono<MainInfoFromStackOverlowDTO> getInfoFromStackOverflow(String url) {
        String questionId = StackOverflowLinkParser.parseQuestionId(url);
        return WEB_CLIENT
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/questions/{id}")
                        .queryParam("site", "stackoverflow")
                        .queryParam("key", STACKOVERFLOW_KEY)
                        .queryParam("access_token", STACKOVERFLOW_ACCESS_TOKEN)
                        .queryParam("filter", "withbody") // Добавляем тело вопроса
                        .build(questionId))
                .retrieve()
                .bodyToMono(StackOverflowResponseDTO.class)
                .map(response -> {
                    if (response.getItems().isEmpty()) {
                        return null;
                    }
                    var question = response.getItems().get(0);
                    String answerPreview = "";
                    if (!question.getAnswers().isEmpty()) {
                        String firstAnswer = question.getAnswers().get(0).getBody();
                        answerPreview =
                                firstAnswer.length() > 200 ? firstAnswer.substring(0, 200) + "..." : firstAnswer;
                    }
                    return new MainInfoFromStackOverlowDTO(
                            question.getTitle(),
                            question.getOwner().getDisplayName(),
                            Instant.ofEpochSecond(question.getCreation_date()),
                            answerPreview);
                });
    }
}
