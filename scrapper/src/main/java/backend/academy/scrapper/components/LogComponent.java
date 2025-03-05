package backend.academy.scrapper.components;

import backend.academy.loggs.LoggFactory;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class LogComponent {
    public static LoggFactory loggFactory;

    public LogComponent() throws IOException {
        loggFactory = new LoggFactory();
    }

    public static LoggFactory getLoggFactory() {
        return loggFactory;
    }
}
