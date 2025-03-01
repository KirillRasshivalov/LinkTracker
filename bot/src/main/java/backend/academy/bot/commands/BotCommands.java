package backend.academy.bot.commands;

import com.pengrad.telegrambot.model.Update;

/**
 * Интерфейс с реализациями различных команд которые поддерживает мой бот.
 */
public interface BotCommands {
    String applyCommand(Update update);
}
