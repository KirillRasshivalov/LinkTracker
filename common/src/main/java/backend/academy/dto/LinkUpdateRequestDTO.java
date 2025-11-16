package backend.academy.dto;

import java.util.List;

public class LinkUpdateRequestDTO {
    Long id;
    String url;
    String description;
    List<Long> tgChatIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTgChatIds(List<Long> tgChatIds) {
        this.tgChatIds = tgChatIds;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public List<Long> getTgChatIds() {
        return tgChatIds;
    }
}
