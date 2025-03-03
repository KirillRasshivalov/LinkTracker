package backend.academy.scrapper.data;

import java.util.List;

/**
 * Рекорд для хранения состояния ссылок
 * @param link - сама ссылка
 * @param filter - филтры
 * @param tags - тэги
 */
public record LinkData(String link, List<String> filter, List<String> tags) {}
