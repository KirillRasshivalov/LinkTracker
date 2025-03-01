package backend.academy.bot.commands;

import com.pengrad.telegrambot.model.Update;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для получения реализации конкретной команды бота.
 */
public class CommandFactory {
    private static Map<String, BotCommands> commands = new HashMap<>();

    static {
        commands.put("/start", new HelloCommand());
        commands.put("/help", new HelpCommand());
        commands.put("/track", new TrackCommand());
        commands.put("/untrack", new UntrackCommand());
        commands.put("/list", new ListCommand());
    }

    public static BotCommands getCommand(String command) {
        return commands.get(command);
    }
}
