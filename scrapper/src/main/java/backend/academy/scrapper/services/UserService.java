package backend.academy.scrapper.services;

import backend.academy.scrapper.jpaRepositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления таблицы с юзерами в бд.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository usersRepo;

    public boolean findUser(Long id) {
        return usersRepo.existsByUserId(id);
    }
}
