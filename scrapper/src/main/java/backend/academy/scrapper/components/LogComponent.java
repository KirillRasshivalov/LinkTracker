package backend.academy.scrapper.components;

import backend.academy.loggs.LoggFactory;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("StaticAssignmentInConstructor")
@SuppressFBWarnings({"MS_CANNOT_BE_FINAL", "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD"})
public class LogComponent {
    public static LoggFactory loggFactory;

    public LogComponent() throws IOException {
        loggFactory = new LoggFactory();
    }

    public static LoggFactory getLoggFactory() {
        return loggFactory;
    }
}
