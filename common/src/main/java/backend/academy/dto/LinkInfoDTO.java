package backend.academy.dto;

import java.util.Arrays;
import java.util.List;

public record LinkInfoDTO(Long id, String url, List<String> tags, List<String> filters) {
    public LinkInfoDTO(Long id, String url, String tags, String filters) {
        this(
                id,
                url,
                tags != null ? Arrays.asList(tags.split(",")) : List.of(),
                filters != null ? Arrays.asList(filters.split(",")) : List.of());
    }
}
