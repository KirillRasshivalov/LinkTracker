package backend.academy.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.Serializable;
import java.util.List;
import lombok.NoArgsConstructor;

@SuppressFBWarnings("PA_PUBLIC_PRIMITIVE_ATTRIBUTE")
@NoArgsConstructor
public class ShowListResponseDTO implements Serializable {
    public List<LinkInfoDTO> links;
    Long size;

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
