package backend.academy.scrapper.notifications;

import java.util.List;
import java.util.Map;

public interface Sender {
    void sendNotification(String link, Map<String, List<Long>> LINKS);
}
