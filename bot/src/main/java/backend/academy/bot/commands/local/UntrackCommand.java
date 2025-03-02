package backend.academy.bot.commands.local;

import com.pengrad.telegrambot.model.Update;

/**
 * Реализация команды удаления ссылки из отслеживаемых.
 */
public class UntrackCommand implements BotCommands {
    @Override
    public String applyCommand(Update update) {
        return "Введите ссылку которую хотите открепить.";
    }
}
