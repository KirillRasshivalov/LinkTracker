package backend.academy.scrapper.configs;


import backend.academy.scrapper.kafka.Producer;
import backend.academy.scrapper.notifications.HTTPSender;
import backend.academy.scrapper.notifications.KafkaSender;
import backend.academy.scrapper.notifications.Sender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфиг для выбора какую реализацию отправки сообщений использовать.
 */
@Configuration
public class TransportConfig {
    @Bean
    @ConditionalOnProperty(name = "app.message.transport", havingValue = "Kafka")
    public Sender kafkaSender(Producer producer) {
        return new KafkaSender(producer);
    }

    @Bean
    @ConditionalOnProperty(name = "app.message.transport", havingValue = "HTTP", matchIfMissing = true)
    public Sender htttpSender() {
        return new HTTPSender();
    }
}
