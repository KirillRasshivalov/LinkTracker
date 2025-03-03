package backend.academy.scrapper.components;

import backend.academy.loggs.LoggFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;

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
