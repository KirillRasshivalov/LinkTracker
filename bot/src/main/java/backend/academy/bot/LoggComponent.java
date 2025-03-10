package backend.academy.bot;

import backend.academy.loggs.LoggFactory;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
@SuppressFBWarnings({"MS_CANNOT_BE_FINAL", "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD"})
@SuppressWarnings("StaticAssignmentInConstructor")
public class LoggComponent {

    public static LoggFactory loggFactory;

    public LoggComponent() throws IOException {
        loggFactory = new LoggFactory();
    }
}
