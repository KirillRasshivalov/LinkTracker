package backend.academy.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import java.util.Map;

/** Класс для добавления кнопок с командами в телеграмм бота. */
public class ListBotCommands {

    public static BotCommand[] getCommandsArray() {

        Map<String, String> commandMap = CommandHandler.COMMANDS;
        BotCommand[] commands = new BotCommand[commandMap.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : commandMap.entrySet()) {
            commands[i] = new BotCommand(entry.getKey(), entry.getValue());
            i++;
        }

        return commands;
    }
}
