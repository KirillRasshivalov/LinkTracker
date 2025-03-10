package backend.academy.scrapper.managers;

import backend.academy.scrapper.data.LinkData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

/** Класс для хранения всх пользователей и их ссылок на протяжения всей сессии. */
@Component
public class Collection {
    public static Map<Long, List<LinkData>> idInfo;
    public static Map<String, List<Long>> linksOwners;
    public static Set<Long> activeUsers;

    public Collection() {
        idInfo = new HashMap<>();
        linksOwners = new HashMap<>();
        activeUsers = new HashSet<>();
    }
}
