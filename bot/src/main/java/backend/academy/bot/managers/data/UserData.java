package backend.academy.bot.managers;

/**
 * Рекорд класс в котором хранится информация о текущем запросе на добавление ссылок.
 * @param link - ссылка
 * @param filter - фильтр
 * @param tag - тэг
 */
public record UserData(String link, String filter, String tag) {}
