package backend.academy.bot.commands;

import backend.academy.bot.commands.export.AddLinkCommand;
import backend.academy.bot.commands.export.DeleteLinkCommand;
import backend.academy.bot.commands.export.RegisterUserCommand;
import backend.academy.bot.commands.export.ServerCommands;
import backend.academy.bot.commands.export.ShowLinksCommand;
import backend.academy.bot.commands.local.BotCommands;
import backend.academy.bot.commands.local.HelloCommand;
import backend.academy.bot.commands.local.HelpCommand;
import backend.academy.bot.commands.local.ListCommand;
import backend.academy.bot.commands.local.TrackCommand;
import backend.academy.bot.commands.local.UntrackCommand;
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
        serverCommands.put("/add_link", new AddLinkCommand());
        serverCommands.put("/register_user", new RegisterUserCommand());
        serverCommands.put("/show_links", new ShowLinksCommand());
        serverCommands.put("/delete_link", new DeleteLinkCommand());
    }

    public static BotCommands getCommand(String command) {
        return botCommands.get(command);
    }

    public static ServerCommands getServerCommand(String command) {
        return serverCommands.get(command);
    }
}
