package backend.academy.bot.commands;

import com.pengrad.telegrambot.model.Update;

/**
 * Реалищация команды добавления текущей ссылки.
 */
public class TrackCommand implements BotCommands {
    @Override
    public String applyCommand(Update update) {
        return "Комманда для добавления ссыллок";
    }
}
