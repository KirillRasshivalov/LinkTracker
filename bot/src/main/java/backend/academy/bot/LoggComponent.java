package backend.academy.bot;

import backend.academy.loggs.LoggFactory;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class LoggComponent {

    public static LoggFactory loggFactory;

    public LoggComponent() throws IOException {
        loggFactory = new LoggFactory();
    }

    public static LoggFactory getLoggFactory() {
        return loggFactory;
    }
}
