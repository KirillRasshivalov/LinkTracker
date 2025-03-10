package backend.academy.loggs;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/** Класс для сохранения логов в один файл. */
public class LoggFactory {

    private static Logger botLogger;
    private static Logger serverLogger;

    static {
        try {
            FileHandler botFileHandler = new FileHandler("BotLogs.log", true);
            FileHandler serverFileHandler = new FileHandler("ServerLogs.log", true);
            botLogger = Logger.getLogger(LoggFactory.class.getName());
            serverLogger = Logger.getLogger(LoggFactory.class.getName());
            botLogger.addHandler(botFileHandler);
            botFileHandler.setFormatter(new SimpleFormatter());
            serverLogger.addHandler(serverFileHandler);
            serverFileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            System.err.println("Failed to initialize log handlers: " + e.getMessage());
            throw new RuntimeException("Log initialization failed", e);
        }
    }

    public LoggFactory() {}

    public static void addBotLog(String message) {
        botLogger.info(message);
    }

    public static void addServerLog(String message) {
        serverLogger.info(message);
    }
}
