package backend.academy.bot;

import static backend.academy.bot.LoggComponent.loggFactory;

import backend.academy.bot.commands.CommandHandler;
import backend.academy.bot.commands.ListBotCommands;
import backend.academy.bot.managers.data.UserData;
import backend.academy.bot.managers.data.UserDataManager;
import backend.academy.bot.managers.state.UserState;
import backend.academy.bot.managers.state.UserStateManager;
import backend.academy.bot.services.UrlValidator;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Основной класс программы, тут выполняется вся логика связанная с общением пользователя и бота, а также реализованы
 * команды позволяющие общаться боту с скрапером.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@SuppressFBWarnings({"ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD"})
public class MyTelegramBot {

    private static final String INCORRECT_URL = "Ссылка не корректна.";
    private static final UserStateManager USER_STATE_MANAGER = new UserStateManager();
    private static final UserDataManager USER_DATA_MANAGER = new UserDataManager();
    private final BotConfig CONFIG;

    private List<String> chatsIds = new ArrayList<>();
    private static TelegramBot bot;

    @PostConstruct
    public void init() throws IOException {
        loggFactory.addBotLog("Зупускаем телеграм бот.");
        bot = new TelegramBot(CONFIG.telegramToken());

        BotCommand[] commands = ListBotCommands.getCommandsArray();

        SetMyCommands setMyCommands = new SetMyCommands(commands);
        bot.execute(setMyCommands);

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                handleUpdate(update);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public static void notificationMessage(@NotNull List<Long> id, @NotNull String message) {
        for (int i = 0; i < id.size(); i++) {
            SendMessage botReply = new SendMessage(id.get(i), message);
            bot.execute(botReply);
        }
    }

    public void handleUpdate(@NotNull Update update) {
        if (update.message() != null && update.message().text() != null) {
            String messageText = update.message().text();
            long chatId = update.message().chat().id();
            UserData userData = USER_DATA_MANAGER.getUserData(chatId);
            loggFactory.addBotLog("Получено " + messageText + " от " + chatId);
            chatsIds.add(String.valueOf(chatId));

            UserState currentState = USER_STATE_MANAGER.getUserState(chatId);
            switch (currentState) {
                case DIALOG:
                    if (messageText.equals("/track")) {
                        USER_STATE_MANAGER.setUserState(chatId, UserState.WAITING_FOR_LINK);
                        SendMessage botReply = new SendMessage(chatId, CommandHandler.getCommandMessage(update));
                        bot.execute(botReply);
                    } else if (messageText.equals("/untrack")) {
                        USER_STATE_MANAGER.setUserState(chatId, UserState.WAITING_FOR_LINK_TO_DELETE);
                        SendMessage botReply = new SendMessage(chatId, CommandHandler.getCommandMessage(update));
                        bot.execute(botReply);
                    } else {
                        SendMessage botReply = new SendMessage(chatId, CommandHandler.getCommandMessage(update));
                        bot.execute(botReply);
                        if (messageText.equals("/start")) {
                            SendMessage botReply2 = new SendMessage(
                                    chatId, CommandHandler.getCommandMessageToServer("/register_user", null, update));
                            bot.execute(botReply2);
                        } else if (messageText.equals("/list")) {
                            SendMessage botReply2 = new SendMessage(
                                    chatId, CommandHandler.getCommandMessageToServer("/show_links", null, update));
                            bot.execute(botReply2);
                        }
                    }
                    break;
                case WAITING_FOR_LINK_TO_DELETE:
                    if (!UrlValidator.isValidUrl(messageText)) {
                        USER_STATE_MANAGER.setUserState(chatId, UserState.DIALOG);
                        bot.execute(new SendMessage(chatId, INCORRECT_URL));
                        break;
                    }
                    userData = new UserData(messageText, userData.filter(), userData.tag());
                    USER_DATA_MANAGER.setUserData(chatId, userData);
                    USER_STATE_MANAGER.setUserState(chatId, UserState.DIALOG);
                    bot.execute(new SendMessage(
                            chatId, CommandHandler.getCommandMessageToServer("/delete_link", messageText, update)));
                    break;
                case WAITING_FOR_LINK:
                    if (!UrlValidator.isValidUrl(messageText)) {
                        USER_STATE_MANAGER.setUserState(chatId, UserState.DIALOG);
                        bot.execute(new SendMessage(chatId, INCORRECT_URL));
                        break;
                    }
                    userData = new UserData(messageText, userData.filter(), userData.tag());
                    USER_DATA_MANAGER.setUserData(chatId, userData);
                    USER_STATE_MANAGER.setUserState(chatId, UserState.WAITING_FOR_FILTER);
                    bot.execute(new SendMessage(chatId, "Теперь введите фильтр:"));
                    break;
                case WAITING_FOR_FILTER:
                    userData = new UserData(userData.link(), messageText, userData.tag());
                    USER_DATA_MANAGER.setUserData(chatId, userData);
                    USER_STATE_MANAGER.setUserState(chatId, UserState.WAITING_FOR_TAG);
                    bot.execute(new SendMessage(chatId, "Теперь введите тег:"));
                    break;
                case WAITING_FOR_TAG:
                    userData = new UserData(userData.link(), userData.filter(), messageText);
                    USER_DATA_MANAGER.setUserData(chatId, userData);
                    USER_STATE_MANAGER.setUserState(chatId, UserState.DIALOG);
                    String fullLink = userData.link() + " < " + userData.filter() + " < " + userData.tag();
                    bot.execute(new SendMessage(
                            chatId, CommandHandler.getCommandMessageToServer("/add_link", fullLink, update)));
                    break;
            }
        }
    }
}
