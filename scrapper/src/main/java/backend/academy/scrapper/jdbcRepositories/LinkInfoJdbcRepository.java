package backend.academy.scrapper.jdbcRepositories;

import backend.academy.dto.LinkInfoDTO;
import backend.academy.scrapper.dataSource.Connecting;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

/** Класс с методами для выполнения запросов к бд. */
@RequiredArgsConstructor
public class LinkInfoJdbcRepository {
    private final Connecting connecting;
    private Connection connection;

    public boolean userHasAnyLinks(Long userId) throws SQLException {
        connection = connecting.createConnection();
        String sql = "SELECT 1 FROM users u JOIN user_links ul ON u.user_id = ul.user_id WHERE u.user_id = ? LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<LinkInfoDTO> findLinksByUser(Long userId) throws SQLException {
        connection = connecting.createConnection();
        String sql = "SELECT li.id, li.link, li.tegs, li.filters "
                + "FROM users u JOIN user_links ul ON u.user_id = ul.user_id "
                + "JOIN link_info li ON ul.link_id = li.id "
                + "WHERE u.user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<LinkInfoDTO> links = new ArrayList<>();
                while (rs.next()) {
                    links.add(new LinkInfoDTO(
                            rs.getLong("id"), rs.getString("link"), rs.getString("tegs"), rs.getString("filters")));
                }
                return links;
            }
        }
    }

    public boolean existsLinkForUser(Long userId, String link) throws SQLException {
        connection = connecting.createConnection();
        String sql = "SELECT 1 FROM users u JOIN user_links ul ON u.user_id = ul.user_id "
                + "JOIN link_info li ON ul.link_id = li.id WHERE u.user_id = ? AND li.link = ? LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setString(2, link);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<LinkInfoDTO> findAllLinks(int page, int size) throws SQLException {
        connection = connecting.createConnection();
        String sql = "SELECT id, link, tegs, filters FROM link_info LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, size);
            stmt.setInt(2, page * size);
            try (ResultSet rs = stmt.executeQuery()) {
                List<LinkInfoDTO> links = new ArrayList<>();
                while (rs.next()) {
                    links.add(new LinkInfoDTO(
                            rs.getLong("id"), rs.getString("link"), rs.getString("tegs"), rs.getString("filters")));
                }
                return links;
            }
        }
    }

    public void deleteByUserAndLink(Long userId, String link) throws SQLException {
        connection = connecting.createConnection();
        String sql = "DELETE FROM link_info WHERE id IN "
                + "(SELECT li.id FROM users u JOIN user_links ul ON u.user_id = ul.user_id "
                + "JOIN link_info li ON ul.link_id = li.id WHERE u.user_id = ? AND li.link = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setString(2, link);
            stmt.executeUpdate();
        }
    }
}
