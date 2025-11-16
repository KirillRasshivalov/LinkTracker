package backend.academy.scrapper.jdbcRepositories;

import backend.academy.scrapper.dataSource.Connecting;
import backend.academy.scrapper.models.Users;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Класс с методами для выполнения запросов к бд. */
@Component
@RequiredArgsConstructor
public class UserJdbcRepository {
    private final Connecting connecting;
    private Connection connection;

    public boolean existsByUserId(Long userId) throws SQLException {
        connection = connecting.createConnection();
        String sql = "SELECT 1 FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void deleteByUserId(Long userId) throws SQLException {
        connection = connecting.createConnection();
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        }
    }

    public Optional<Users> findByUserId(Long userId) throws SQLException {
        connection = connecting.createConnection();
        String sql = "SELECT id, user_id FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Users user = new Users();
                    user.id(rs.getLong("id"));
                    user.userId(rs.getLong("user_id"));
                    return Optional.of(user);
                }
                return Optional.empty();
            }
        }
    }

    public void save(Long userId) throws SQLException {
        connection = connecting.createConnection();
        String sql = "INSERT INTO users (user_id) VALUES (?) ON CONFLICT (user_id) DO NOTHING";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        }
    }

    public void addLinkToUser(Long userId, Long linkId) throws SQLException {
        connection = connecting.createConnection();
        String sql = "INSERT INTO user_links (user_id, link_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, linkId);
            stmt.executeUpdate();
        }
    }
}
