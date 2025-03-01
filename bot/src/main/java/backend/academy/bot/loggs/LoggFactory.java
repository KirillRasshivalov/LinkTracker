package backend.academy.bot.loggs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Класс для сохранения логов в один файл.
 */
@Component
public class LoggFactory {

    @Getter @Setter
    private static Logger logger = Logger.getLogger(LoggFactory.class.getName());

    public LoggFactory() throws IOException {
        FileHandler fileHandler = new FileHandler("Logs.log", true);
        logger.addHandler(fileHandler);
        fileHandler.setFormatter(new SimpleFormatter());
    }

    public static void addLog(String message) {
        logger.info(message);
    }
}
