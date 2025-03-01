package backend.academy.bot.commands;

import com.pengrad.telegrambot.model.Update;

import java.util.Map;

/**
 * Класс для хранения информации о командах и всей логикой связанной с ними.
 */
public class CommandHandler {

    public static final Map<String, String> commands = Map.of(
        "/start", "регистрация пользователя.",
        "/help", "вывод списка доступных команд.",
        "/track", "начать отслеживание ссылки.",
        "/untrack", "прекратить отслеживание ссылки.",
        "/list", "показать список отслеживаемых ссылок."
    );

    public static String getListOfCommands() {
        StringBuilder listOfCommands = new StringBuilder();
        for (String command : commands.keySet()) {
            listOfCommands.append(command).append(" - ").append(commands.get(command));
            listOfCommands.append("\n");
        }
        return listOfCommands.toString();
    }

    public static String getCommandMessage(Update update) {
        if (commands.containsKey(update.message().text())) {
            BotCommands currCommand = CommandFactory.getCommand(update.message().text());
            return currCommand.applyCommand(update);
        }
        return "Данная команда не поддерживается.";
    }
}
