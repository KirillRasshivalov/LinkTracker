package backend.academy.bot.managers;

/**
 * Енамы состояний запроса.
 */
public enum UserState {
    DIALOG,
    WAITING_FOR_LINK,
    WAITING_FOR_LINK_TO_DELETE,
    WAITING_FOR_FILTER,
    WAITING_FOR_TAG
}
