package backend.academy.dto;

import java.time.Instant;

public record MainInfoFromGithubDTO(String message, String authorName, Instant createdAt, String nameOfIssue) {}
