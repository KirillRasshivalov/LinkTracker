package backend.academy.bot.commands.local;

import com.pengrad.telegrambot.model.Update;
import jakarta.validation.constraints.NotNull;

/** Реалищация команды добавления текущей ссылки. */
public class TrackCommand implements BotCommands {
    @Override
    public String applyCommand(@NotNull Update update) {
        return "Введите ссылку для отслеживания.";
    }
}
