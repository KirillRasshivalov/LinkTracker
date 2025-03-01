package backend.academy.bot.commands;

import com.pengrad.telegrambot.model.Update;

/**
 * Реализация команды помощи.
 */
public class HelpCommand implements BotCommands {
    @Override
    public String applyCommand(Update update) {
        return CommandHandler.getListOfCommands();
    }
}
