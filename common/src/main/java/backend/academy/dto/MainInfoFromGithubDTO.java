package backend.academy.dto;

import java.time.Instant;

/**
 * Класс для общей информации об обновлении для гитхаба.
 * @param message
 * @param authorName
 * @param createdAt
 * @param nameOfAnswer
 * @param type
 */
public record MainInfoFromGithubDTO(
        String message, String authorName, Instant createdAt, String nameOfAnswer, String type) {}
