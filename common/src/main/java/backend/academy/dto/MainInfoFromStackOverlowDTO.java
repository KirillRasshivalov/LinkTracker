package backend.academy.dto;

import java.time.Instant;

/**
 * Класс для общей информации ответа от стек оверфлоу.
 * @param theme
 * @param name
 * @param time
 * @param answer
 */
public record MainInfoFromStackOverlowDTO(String theme, String name, Instant time, String answer) {}
