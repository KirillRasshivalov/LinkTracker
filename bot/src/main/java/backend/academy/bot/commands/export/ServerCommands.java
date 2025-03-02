package backend.academy.bot.commands.export;

import com.pengrad.telegrambot.model.Update;

/**
 * Интерфейс для команд которые будут отправляться на скрепер и ждать ответ от него.
 */
public interface ServerCommands {
    String applyCommand(String link, Update update);
}
