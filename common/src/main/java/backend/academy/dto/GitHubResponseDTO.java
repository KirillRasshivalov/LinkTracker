package backend.academy.dto;

import java.time.Instant;

public class GitHubResponseDTO {
    private String sha;
    private CommitDTO commit;

    public static class CommitDTO {
        private AuthorDTO author;
        private String message;

        public static class AuthorDTO {
            private String name;

            public void setName(String name) {
                this.name = name;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public void setDate(Instant date) {
                this.date = date;
            }

            public String getName() {
                return name;
            }

            public String getEmail() {
                return email;
            }

            public Instant getDate() {
                return date;
            }

            private String email;
            private Instant date;
        }

        public Instant getDate() {
            return author != null ? author.date : null;
        }

        public void setAuthor(AuthorDTO author) {
            this.author = author;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public AuthorDTO getAuthor() {
            return author;
        }

        public String getMessage() {
            return message;
        }
    }

    public CommitDTO getCommit() {
        return commit;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public void setCommit(CommitDTO commit) {
        this.commit = commit;
    }

    public String getSha() {
        return sha;
    }
}
