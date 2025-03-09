package backend.academy.loggs;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Класс для сохранения логов в один файл.
 */
public class LoggFactory {

    private static Logger botLogger = Logger.getLogger(LoggFactory.class.getName());
    private static Logger serverLogger = Logger.getLogger(LoggFactory.class.getName());

    public LoggFactory() throws IOException {
        FileHandler botFfileHandler = new FileHandler("BotLogs.log", true);
        FileHandler serverFileHandler = new FileHandler("ServerLogs.log", true);
        botLogger.addHandler(botFfileHandler);
        botFfileHandler.setFormatter(new SimpleFormatter());
        serverLogger.addHandler(serverFileHandler);
        serverFileHandler.setFormatter(new SimpleFormatter());
    }

    public static void addBotLog(String message) {
        botLogger.info(message);
    }

    public static void addServerLog(String message) {
        serverLogger.info(message);
    }
}
