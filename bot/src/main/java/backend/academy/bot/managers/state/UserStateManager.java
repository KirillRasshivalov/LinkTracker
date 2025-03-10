package backend.academy.bot.managers.state;

import java.util.HashMap;
import java.util.Map;

/** Класс для поддержания состояния текущего пользователя. */
public class UserStateManager {

    private final Map<Long, UserState> userStates = new HashMap<>();

    public void setUserState(long chatId, UserState state) {
        userStates.put(chatId, state);
    }

    public UserState getUserState(long chatId) {
        return userStates.getOrDefault(chatId, UserState.DIALOG);
    }

    public void clearUserState(long chatId) {
        userStates.remove(chatId);
    }
}
