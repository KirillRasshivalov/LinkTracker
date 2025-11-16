package backend.academy.bot.managers.data;

import java.util.HashMap;
import java.util.Map;

/** Класс для сохранения данных конкретного пользователя по отправки ссылки. */
public class UserDataManager {

    private final Map<Long, UserData> userDataMap = new HashMap<>();

    public void setUserData(long chatId, UserData data) {
        userDataMap.put(chatId, data);
    }

    public UserData getUserData(long chatId) {
        return userDataMap.getOrDefault(chatId, new UserData(null, null, null));
    }

    public void clearUserData(long chatId) {
        userDataMap.remove(chatId);
    }
}
