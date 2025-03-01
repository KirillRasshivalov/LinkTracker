package backend.academy.bot.commands;

import com.pengrad.telegrambot.model.Update;

/**
 * Реалищация команды вывода ссылок.
 */
public class ListCommand implements BotCommands{
    @Override
    public String applyCommand(Update update) {
        return "список";
    }
}
