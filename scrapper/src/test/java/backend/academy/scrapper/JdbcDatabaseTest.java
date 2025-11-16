package backend.academy.scrapper;

import static org.junit.jupiter.api.Assertions.*;

import backend.academy.dto.LinkInfoDTO;
import backend.academy.scrapper.jdbcRepositories.LinkInfoJdbcRepository;
import backend.academy.scrapper.jdbcRepositories.UserJdbcRepository;
import backend.academy.scrapper.managers.Datasourse;
import backend.academy.scrapper.managers.JdbcDatabase;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "app.access-type=JDBC")
public class JdbcDatabaseTest extends AbstractIntegrationTest {

    @Autowired
    private Datasourse datasourse;

    @Test
    void shouldAddAndFindUser() {
        Long userId = 123L;
        datasourse.addUser(userId);

        List<LinkInfoDTO> links = datasourse.showLinks(userId);
        assertTrue(links.isEmpty());
    }

    //    @Test
    //    void shouldAddAndDeleteLink() {
    //        Long userId = 456L;
    //        String testUrl = "https://example.com";
    //
    //        datasourse.addUser(userId);
    //        datasourse.addLink(userId, testUrl, "filter", "tag");
    //
    //        List<LinkInfoDTO> links = datasourse.showLinks(userId);
    //        assertEquals(1, links.size());
    //        assertEquals(testUrl, links.get(0).url());
    //
    //        datasourse.deleteLink(userId, testUrl);
    //        assertTrue(datasourse.showLinks(userId).isEmpty());
    //    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public Datasourse testjdbcDatasource(UserJdbcRepository userRepo, LinkInfoJdbcRepository linkRepo) {
            return new JdbcDatabase(userRepo, linkRepo);
        }
    }
}
