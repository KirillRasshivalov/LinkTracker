package backend.academy.scrapper;

import static org.junit.jupiter.api.Assertions.*;

import backend.academy.scrapper.configs.DatabaseConfig;
import backend.academy.scrapper.managers.Datasourse;
import backend.academy.scrapper.managers.JdbcDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "app.access-type=JDBC")
public class DatabaseConfigTest extends AbstractIntegrationTest {

    @Autowired
    private Datasourse datasourse;

    @Test
    void shouldUseJdbcWhenPropertySet() {
        assertTrue(datasourse instanceof JdbcDatabase);
    }

    @TestConfiguration
    static class OverrideConfig {
        @Bean
        public DatabaseConfig databaseConfig() {
            return new DatabaseConfig();
        }
    }
}
