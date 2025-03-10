package backend.academy.dto;

import java.util.List;

public class DeleteLinkResponceDTO {
    Long id;
    String url;
    List<String> tags;
    List<String> filters;

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }
}
