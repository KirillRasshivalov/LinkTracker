package backend.academy.dto;


import java.util.List;

public class AddLinkResponseDTO {
    public String id;
    public String url;
    public List<String> tags;
    public List<String> filters;

    public void setId(String id) {
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

    public String getId() {
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
}

