package backend.academy.bot.commands;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для получения реализации конкретной команды бота.
 */
public class CommandFactory {
    private static Map<String, BotCommands> botCommands = new HashMap<>();

    private static Map<String, ServerCommands> serverCommands = new HashMap<>();

    static {
        botCommands.put("/start", new HelloCommand());
        botCommands.put("/help", new HelpCommand());
        botCommands.put("/track", new TrackCommand());
        botCommands.put("/untrack", new UntrackCommand());
        botCommands.put("/list", new ListCommand());
        serverCommands.put("/update_link", new UpdateCommand());
    }

    public static BotCommands getCommand(String command) {
        return botCommands.get(command);
    }

    public static ServerCommands getServerCommand(String command) {
        return serverCommands.get(command);
    }
}
