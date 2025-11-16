package backend.academy.scrapper.notifications;

import java.util.List;

/** Интерфейс для общения скрапера и бота. */
public interface Sender {
    void sendNotification(String link, List<Long> IDS, Object info);
}
