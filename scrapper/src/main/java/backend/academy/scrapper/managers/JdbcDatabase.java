package backend.academy.scrapper.managers;

import backend.academy.dto.LinkInfoDTO;
import backend.academy.scrapper.jdbcRepositories.LinkInfoJdbcRepository;
import backend.academy.scrapper.jdbcRepositories.UserJdbcRepository;
import backend.academy.scrapper.models.LinkInfo;
import backend.academy.scrapper.services.ServerLogger;
import jakarta.transaction.Transactional;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Класс для реализации методов для общения с бд, с использованием jdbc.
 */
@Transactional
@RequiredArgsConstructor
public class JdbcDatabase implements Datasourse {
    private final UserJdbcRepository userJdbcRepository;
    private final LinkInfoJdbcRepository linkInfoJdbcRepository;

    @Override
    public void addUser(Long id) {
        try {
            if (!userJdbcRepository.existsByUserId(id)) {
                userJdbcRepository.save(id);
            }
        } catch (SQLException e) {
            ServerLogger.LOGGER
                    .atError()
                    .setMessage("Ошибка при добавление юзера: " + e.getMessage())
                    .log();
        }
    }

    @Override
    public void deleteUser(Long id) {
        try {
            linkInfoJdbcRepository.deleteAllUserLinks(id);
            userJdbcRepository.deleteByUserId(id);
        } catch (SQLException e) {
            ServerLogger.LOGGER
                    .atError()
                    .setMessage("Ошибка при удаление юзера: " + e.getMessage())
                    .log();
        }
    }

    @Override
    public void addLink(Long userId, String link, String filter, String teg) {
        try {
            if (!userJdbcRepository.existsByUserId(userId)) {
                ServerLogger.LOGGER
                        .atError()
                        .setMessage("Ошибка, юзер с таким айди не найден.")
                        .log();
            }
            Long linkId = linkInfoJdbcRepository.save(link, filter, teg);
            userJdbcRepository.addLinkToUser(userId, linkId);
        } catch (SQLException e) {
            ServerLogger.LOGGER
                    .atError()
                    .setMessage("Ошибка при добавление ссылки: " + e.getMessage())
                    .log();
        }
    }

    @Override
    public void deleteLink(Long userId, String link) {
        try {
            if (!linkInfoJdbcRepository.existsLinkForUser(userId, link)) {
                ServerLogger.LOGGER
                        .atError()
                        .setMessage("Ошибка, ссылка не пренадлжит никакому юзеру.")
                        .log();
            }
            linkInfoJdbcRepository.deleteByUserAndLink(userId, link);
        } catch (SQLException e) {
            ServerLogger.LOGGER
                    .atError()
                    .setMessage("Ошибка при удаление ссылки: " + e.getMessage())
                    .log();
        }
    }

    @Override
    public List<LinkInfoDTO> showLinks(Long userId) {
        try {
            return linkInfoJdbcRepository.findLinksByUser(userId);
        } catch (SQLException e) {
            ServerLogger.LOGGER
                    .atError()
                    .setMessage("Ошибка при показе ссылок: " + e.getMessage())
                    .log();
        }
        return new ArrayList<>();
    }

    @Override
    public Page<LinkInfo> showLinks(Pageable pageable) {
        try {
            List<LinkInfoDTO> linkDTOs =
                    linkInfoJdbcRepository.findAllLinks((int) pageable.getOffset(), pageable.getPageSize());
            List<LinkInfo> links = linkDTOs.stream()
                    .map(dto -> {
                        LinkInfo link = new LinkInfo();
                        link.id(dto.id());
                        link.link(dto.url());
                        link.filters(dto.filters().get(0));
                        link.tegs(dto.tags().get(0));
                        return link;
                    })
                    .collect(Collectors.toList());
            int total = linkInfoJdbcRepository.countAllLinks();
            return new PageImpl<>(links, pageable, total);
        } catch (SQLException e) {
            ServerLogger.LOGGER
                    .atError()
                    .setMessage("Ошибка при показе: " + e.getMessage())
                    .log();
        }
        return null;
    }
}
