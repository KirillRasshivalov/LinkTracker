package backend.academy.scrapper.jpaRepositories;

import backend.academy.dto.LinkInfoDTO;
import backend.academy.scrapper.models.LinkInfo;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LinkInfoRepository extends JpaRepository<LinkInfo, Long> {
    @Query("SELECT CASE WHEN COUNT(li) > 0 THEN true ELSE false END " + "FROM Users u JOIN u.links li "
            + "WHERE u.userId = :userId")
    boolean userHasAnyLinks(@Param("userId") Long userId);

    @Query("SELECT new backend.academy.dto.LinkInfoDTO(" + "li.id, li.link, li.tegs, li.filters) "
            + "FROM Users u JOIN u.links li "
            + "WHERE u.userId = :userId")
    List<LinkInfoDTO> findLinksByUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(l) > 0 FROM Users u JOIN u.links l WHERE u.userId = :userId AND l.link = :link")
    boolean existsLinkForUser(@Param("userId") Long userId, @Param("link") String link);

    @Query("SELECT li FROM LinkInfo li")
    Page<LinkInfo> findAllLinks(Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM LinkInfo l WHERE l IN "
            + "(SELECT li FROM Users u JOIN u.links li WHERE u.userId = :userId AND li.link = :link)")
    void deleteByUserAndLink(@Param("userId") Long userId, @Param("link") String link);
}
