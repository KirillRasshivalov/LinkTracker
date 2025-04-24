package backend.academy.scrapper.notifications;

import backend.academy.dto.MainInfoFromGithubDTO;
import java.util.List;

/** Интерфейс для общения скрапера и бота. */
public interface Sender {
    void sendNotification(String link, List<Long> IDS, MainInfoFromGithubDTO info);
}
