package backend.academy.bot.commands.local;

import com.pengrad.telegrambot.model.Update;

/** Реализация команды регистрации. */
public class HelloCommand implements BotCommands {
    @Override
    public String applyCommand(Update update) {
        return "Привет, " + update.message().from().firstName() + "! Это бот для отслеживания ссылок. "
                + "Для того чтобы узнать какие команды есть, введите /help";
    }
}
