package backend.academy.scrapper.managers;

/** Класс для проверки принаждежности ссылки. */
public class CheckLink {
    public boolean isGitHubLink(String link) {
        return link.startsWith("https://github.com/");
    }

    public boolean isStackOverflowLink(String link) {
        return link.startsWith("https://stackoverflow.com/");
    }
}
