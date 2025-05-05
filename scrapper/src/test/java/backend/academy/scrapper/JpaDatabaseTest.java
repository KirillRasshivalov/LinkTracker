package backend.academy.scrapper;

import static org.junit.jupiter.api.Assertions.*;

import backend.academy.dto.LinkInfoDTO;
import backend.academy.scrapper.jpaRepositories.LinkInfoRepository;
import backend.academy.scrapper.jpaRepositories.UsersRepository;
import backend.academy.scrapper.managers.Datasourse;
import backend.academy.scrapper.managers.JpaDatabase;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "app.access-type=JPA")
public class JpaDatabaseTest extends AbstractIntegrationTest {

    @Autowired
    private Datasourse datasourse;

    @Test
    void shouldHandleUserLinksWithJpa() {
        Long userId = 789L;
        String testUrl = "https://jpa-test.com";

        datasourse.addUser(userId);
        datasourse.addLink(userId, testUrl, "jpa-filter", "jpa-tag");

        List<LinkInfoDTO> links = datasourse.showLinks(userId);
        assertEquals(1, links.size());
        assertEquals("jpa-filter", links.getFirst().filters().getFirst());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public Datasourse testJpaDatasource(UsersRepository usersRepo, LinkInfoRepository linksRepo) {
            return new JpaDatabase(usersRepo, linksRepo);
        }
    }
}
