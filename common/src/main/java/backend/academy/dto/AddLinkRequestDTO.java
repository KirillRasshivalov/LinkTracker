package backend.academy.dto;

import java.util.List;

public class AddLinkRequestDTO {
    String link;
    List<String> tags;
    List<String> filters;

    public void setLink(String link) {
        this.link = link;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public String getLink() {
        return link;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getFilters() {
        return filters;
    }
}
