package backend.academy.scrapper.managers;

/** Класс для парсинга ссылок на стековерфлоу. */
@SuppressWarnings("StringSplitter")
public class StackOverflowLinkParser {
    public static String parseQuestionId(String url) {
        String[] parts = url.split("/");
        if (parts.length < 5 || !parts[3].equals("questions")) {
            throw new IllegalArgumentException(url);
        }
        return parts[4];
    }
}
