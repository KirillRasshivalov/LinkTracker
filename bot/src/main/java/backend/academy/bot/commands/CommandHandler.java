package backend.academy.bot.commands;

import backend.academy.bot.commands.export.ServerCommands;
import backend.academy.bot.commands.local.BotCommands;
import com.pengrad.telegrambot.model.Update;
import java.util.List;
import java.util.Map;

/** Класс для хранения информации о командах и всей логикой связанной с ними. */
public class CommandHandler {

    public static final Map<String, String> COMMANDS = Map.of(
            "/start", "регистрация пользователя.",
            "/help", "вывод списка доступных команд.",
            "/track", "начать отслеживание ссылки.",
            "/untrack", "прекратить отслеживание ссылки.",
            "/list", "показать список отслеживаемых ссылок.");

    private static final List<String> COMMANDS_TO_SERVER =
            List.of("/add_link", "/register_user", "/show_links", "/delete_link");

    public static String getListOfCommands() {
        StringBuilder listOfCommands = new StringBuilder();
        for (String command : COMMANDS.keySet()) {
            listOfCommands.append(command).append(" - ").append(COMMANDS.get(command));
            listOfCommands.append("\n");
        }

        return listOfCommands.toString();
    }

    public static String getCommandMessage(Update update) {
        if (COMMANDS.containsKey(update.message().text())) {
            BotCommands currCommand = CommandFactory.getCommand(update.message().text());

            return currCommand.applyCommand(update);
        }

        return "Данная команда не поддерживается.";
    }

    public static String getCommandMessageToServer(String link, String message, Update update) {
        if (COMMANDS_TO_SERVER.contains(link)) {
            ServerCommands serverCommands = CommandFactory.getServerCommand(link);

            return serverCommands.applyCommand(message, update);
        }

        return null;
    }
}
