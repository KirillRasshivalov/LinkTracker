package backend.academy.dto;

import java.util.List;

public class ShowListResponseDTO {
    public List<LinkInfoDTO> links;
    public Long size;


    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public List<LinkInfoDTO> getLinks() {
        return links;
    }

    public void setLinks(List<LinkInfoDTO> links) {
        this.links = links;
    }
}
