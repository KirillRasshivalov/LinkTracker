package backend.academy.scrapper.services;

import backend.academy.scrapper.jpaRepositories.LinkInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Сервис для управления таблицы с ссылками. */
@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkInfoRepository linkInfoRepository;

    public boolean findLink(String link, Long userId) {
        return linkInfoRepository.existsLinkForUser(userId, link);
    }

    public boolean findConnectedLinks(Long userId) {
        return linkInfoRepository.userHasAnyLinks(userId);
    }
}
