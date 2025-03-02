package backend.academy.bot.commands;

/**
 * Интерфейс для команд которые будут отправляться на скрепер и ждать ответ от него.
 */
public interface ServerCommands {
    String applyCommand(String link);
}
