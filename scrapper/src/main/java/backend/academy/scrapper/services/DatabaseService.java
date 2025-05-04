package backend.academy.scrapper.services;

import backend.academy.dto.LinkInfoDTO;
import backend.academy.scrapper.managers.Datasourse;
import backend.academy.scrapper.models.LinkInfo;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с репозиториями базы данных.
 */
@Service
@RequiredArgsConstructor
public class DatabaseService {
    private final Datasourse database;

    public void addUser(Long id) {
        database.addUser(id);
    }

    public void deleteUser(Long id) {
        database.deleteUser(id);
    }

    @Transactional
    public void addLink(Long userId, String link, String filter, String teg) {
        database.addLink(userId, link, filter, teg);
    }

    @Transactional
    public void deleteLink(Long userId, String link) {
        database.deleteLink(userId, link);
    }

    public List<LinkInfoDTO> showLinks(Long userId) {
        return database.showLinks(userId);
    }

    @Transactional
    public Page<LinkInfo> showLinks(Pageable pageable) {
        return database.showLinks(pageable);
    }
}
