package backend.academy.bot;

import backend.academy.bot.commands.CommandHandler;
import backend.academy.bot.commands.ListBotCommands;
import backend.academy.bot.managers.data.UserData;
import backend.academy.bot.managers.data.UserDataManager;
import backend.academy.bot.managers.state.UserState;
import backend.academy.bot.managers.state.UserStateManager;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static backend.academy.bot.LoggComponent.loggFactory;

/**
 * Основной класс программы, тут выполняется вся логика связанная с общением пользователя и бота, а также реализованы
 * команды позволяющие общаться боту с скрапером.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MyTelegramBot {

    private List<String> chatsIds = new ArrayList<>();

    private TelegramBot bot;

    private final BotConfig config;

    private final UserStateManager userStateManager = new UserStateManager();
    private final UserDataManager userDataManager = new UserDataManager();

    @PostConstruct
    public void init() throws IOException {
        loggFactory.addLog("Зупускаем телеграм бот.");
        bot = new TelegramBot(config.telegramToken());

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

    private void handleUpdate(@NotNull Update update) {
        if (update.message() != null && update.message().text() != null) {
            String messageText = update.message().text();
            long chatId = update.message().chat().id();
            UserData userData = userDataManager.getUserData(chatId);
            loggFactory.addLog("Получено " + messageText + " от " + chatId);
            chatsIds.add(String.valueOf(chatId));

            UserState currentState = userStateManager.getUserState(chatId);
            switch (currentState) {
                case DIALOG:
                    if (messageText.equals("/track")) {
                        userStateManager.setUserState(chatId, UserState.WAITING_FOR_LINK);
                        SendMessage botReply = new SendMessage(chatId, CommandHandler.getCommandMessage(update));
                        bot.execute(botReply);
                    } else if (messageText.equals("/untrack")) {
                        userStateManager.setUserState(chatId, UserState.WAITING_FOR_LINK_TO_DELETE);
                        SendMessage botReply = new SendMessage(chatId, CommandHandler.getCommandMessage(update));
                        bot.execute(botReply);
                    } else {
                        SendMessage botReply = new SendMessage(chatId, CommandHandler.getCommandMessage(update));
                        bot.execute(botReply);
                    }
                    break;
                case WAITING_FOR_LINK_TO_DELETE:
                    userData = new UserData(messageText, userData.filter(), userData.tag());
                    userDataManager.setUserData(chatId, userData);
                    userStateManager.setUserState(chatId, UserState.DIALOG);
                    bot.execute(new SendMessage(chatId, CommandHandler.getCommandMessage(update)));
                    break;
                case WAITING_FOR_LINK:
                    userData = new UserData(messageText, userData.filter(), userData.tag());
                    userDataManager.setUserData(chatId, userData);
                    userStateManager.setUserState(chatId, UserState.WAITING_FOR_FILTER);
                    bot.execute(new SendMessage(chatId, "Теперь введите фильтр:"));
                    break;
                case WAITING_FOR_FILTER:
                    userData = new UserData(userData.link(), messageText, userData.tag());
                    userDataManager.setUserData(chatId, userData);
                    userStateManager.setUserState(chatId, UserState.WAITING_FOR_TAG);
                    bot.execute(new SendMessage(chatId, "Теперь введите тег:"));
                    break;
                case WAITING_FOR_TAG:
                    userData = new UserData(userData.link(), userData.filter(), messageText);
                    userDataManager.setUserData(chatId, userData);
                    userStateManager.setUserState(chatId, UserState.DIALOG);
                    String fullLink = userData.link() + "<" + userData.filter() + "<" + userData.tag();
                    bot.execute(new SendMessage(chatId, CommandHandler.getCommandMessageToServer("/add_link", fullLink, update)));
                    break;
            }
        }
    }
}
