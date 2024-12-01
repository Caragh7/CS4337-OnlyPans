package onlypans.emailNotificationService.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// This class sets up the connection between the app and RabbitMQ
@Configuration
public class RabbitMQConfig {

    // Read values from app properties
    @Value("${rabbitmq.queue.email.name}")
    private String emailQueue;

    @Value("${rabbitmq.exchange.email.name}")
    private String emailExchange;

    @Value("${rabbitmq.binding.email.name}")
    private String emailRoutingKey;

    @Bean
    public Queue emailQueue() {
        // store messages for processing
        return new Queue(emailQueue, true);
    }

    @Bean
    public TopicExchange emailExchange() {
        // route messages to the appropriate queue based on the routing key
        return new TopicExchange(emailExchange);
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, TopicExchange emailExchange) {
        // Ensures messages sent to the exchange with the correct routing key
        // Will land in the queue
        return BindingBuilder.bind(emailQueue)
                .to(emailExchange)
                .with(emailRoutingKey);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        // Configures a template to send messages to RabbitMQ
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        // This will EmailDetailDTO into JSON format
        return new Jackson2JsonMessageConverter();
    }
}
