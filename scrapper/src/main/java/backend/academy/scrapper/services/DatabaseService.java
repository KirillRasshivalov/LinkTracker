package backend.academy.scrapper.services;

import backend.academy.dto.LinkInfoDTO;
import backend.academy.scrapper.jpaRepositories.LinkInfoRepository;
import backend.academy.scrapper.jpaRepositories.UsersRepository;
import backend.academy.scrapper.models.LinkInfo;
import backend.academy.scrapper.models.Users;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseService {
    private final UsersRepository usersRepo;
    private final LinkInfoRepository linksRepo;

    public void addUser(Long id) {
        Users user = new Users();
        user.userId(id);
        usersRepo.save(user);
    }

    public void deleteUser(Long id) {
        usersRepo.deleteByUserId(id);
        usersRepo.findByUserId(id).ifPresent(usersRepo::delete);
    }

    @Transactional
    public void addLink(Long userId, String link, String filter, String teg) {
        Users user = usersRepo
                .findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        LinkInfo linkInfo = new LinkInfo();
        linkInfo.link(link);
        linkInfo.filters(filter);
        linkInfo.tegs(teg);
        linksRepo.save(linkInfo);

        user.links().add(linkInfo);
        linkInfo.users().add(user);
        usersRepo.save(user);
    }

    @Transactional
    public void deleteLink(Long userId, String link) {
        linksRepo.deleteByUserAndLink(userId, link);
    }

    public List<LinkInfoDTO> showLinks(Long userId) {
        return linksRepo.findLinksByUser(userId);
    }

    @Transactional
    public Page<LinkInfo> showLinks(Pageable pageable) {
        return linksRepo.findAllLinks(pageable);
    }
}
