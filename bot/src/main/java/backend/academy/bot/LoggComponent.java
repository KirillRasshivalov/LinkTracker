package backend.academy.bot;

import backend.academy.loggs.LoggFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;

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
