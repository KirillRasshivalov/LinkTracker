package backend.academy.scrapper.configs;

import backend.academy.scrapper.jdbcRepositories.LinkInfoJdbcRepository;
import backend.academy.scrapper.jdbcRepositories.UserJdbcRepository;
import backend.academy.scrapper.jpaRepositories.LinkInfoRepository;
import backend.academy.scrapper.jpaRepositories.UsersRepository;
import backend.academy.scrapper.managers.Datasourse;
import backend.academy.scrapper.managers.JdbcDatabase;
import backend.academy.scrapper.managers.JpaDatabase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Конфиг для выбора какую реализация общения с бд использовать в программе. */
@Configuration
public class DatabaseConfig {
    @Bean
    @ConditionalOnProperty(name = "app.access-type", havingValue = "JPA")
    public Datasourse jpaDatasource(UsersRepository usersRepo, LinkInfoRepository linksRepo) {
        return new JpaDatabase(usersRepo, linksRepo);
    }

    @Bean
    @ConditionalOnProperty(name = "app.access-type", havingValue = "JDBC", matchIfMissing = true)
    public Datasourse jdbcDatasource(UserJdbcRepository usersRepo, LinkInfoJdbcRepository linksRepo) {
        return new JdbcDatabase(usersRepo, linksRepo);
    }
}
