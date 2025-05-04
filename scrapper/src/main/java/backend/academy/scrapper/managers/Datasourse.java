package backend.academy.scrapper.managers;

import backend.academy.dto.LinkInfoDTO;
import backend.academy.scrapper.models.LinkInfo;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Интерфейс для реализации методов обращения к бд.
 */
public interface Datasourse {
    void addUser(Long id);

    void deleteUser(Long id);

    void addLink(Long userId, String link, String filter, String teg);

    void deleteLink(Long userId, String link);

    List<LinkInfoDTO> showLinks(Long userId);

    Page<LinkInfo> showLinks(Pageable pageable);
}
