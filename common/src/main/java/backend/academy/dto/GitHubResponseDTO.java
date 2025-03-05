package backend.academy.dto;

import java.time.Instant;

public class GitHubResponseDTO {

    private String sha;
    private CommitDTO commit;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public CommitDTO getCommit() {
        return commit;
    }

    public void setCommit(CommitDTO commit) {
        this.commit = commit;
    }
}
