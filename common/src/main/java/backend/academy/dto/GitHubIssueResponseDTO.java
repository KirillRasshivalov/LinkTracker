package backend.academy.dto;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubIssueResponseDTO {
    private String title;
    private UserDTO user;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    public String getTitle() {
        return title;
    }

    public UserDTO getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    @Getter
    @Setter
    public static class UserDTO {
        private String login;

        public void setLogin(String login) {
            this.login = login;
        }

        public String getLogin() {
            return login;
        }
    }

    private String body;
    private Instant created_at;
}
