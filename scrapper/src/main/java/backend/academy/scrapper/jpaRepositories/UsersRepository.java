package backend.academy.scrapper.jpaRepositories;

import backend.academy.scrapper.models.Users;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/** Репозиторий для выполнения методов, которые требуют запросы в бд по таблице юзеров. */
public interface UsersRepository extends JpaRepository<Users, Long> {
    boolean existsByUserId(Long userId);

    @Override
    void deleteById(Long userId);

    @Modifying
    @Query("DELETE FROM Users u WHERE u.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    Optional<Users> findByUserId(Long userId);
}
