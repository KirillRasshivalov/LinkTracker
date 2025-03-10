package backend.academy.scrapper.managers;

/** Класс для парсинга ссылок на гитхаб. */
@SuppressWarnings("StringSplitter")
public class GitHubLinkParser {
    public static GitHubLink parse(String url) {
        String[] parts = url.split("/");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid GitHub URL: " + url);
        }
        String owner = parts[3];
        String repo = parts[4];

        return new GitHubLink(owner, repo);
    }

    public static class GitHubLink {
        private final String owner;
        private final String repo;

        public GitHubLink(String owner, String repo) {
            this.owner = owner;
            this.repo = repo;
        }

        public String getOwner() {
            return owner;
        }

        public String getRepo() {
            return repo;
        }
    }
}
