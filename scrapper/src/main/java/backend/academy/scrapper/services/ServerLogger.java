package backend.academy.scrapper.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ServerLogger implements CommandLineRunner {

    public static Logger LOGGER = LoggerFactory.getLogger(ServerLogger.class);

    @Override
    public void run(String... args) throws Exception {
        LOGGER.atInfo()
                .setMessage("Structure logging")
                .addKeyValue("userId", "1")
                .log();
    }
}
