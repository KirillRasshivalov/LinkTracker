package backend.academy.scrapper.dataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import backend.academy.scrapper.DataBaseConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Компонент который будет создавать connection с базой данных.
 */
@Component
@RequiredArgsConstructor
public class Connecting {
    private final DataBaseConfig config;

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(config.url(), config.username(), config.password());
    }
}
