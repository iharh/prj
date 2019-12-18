package ing.conf;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamMessageConverter;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.MessageConverter;

import java.util.Optional;

@Configuration
@EnableBinding(Processor.class)
public class StreamConfiguration {
    private final int retryAttempts;

    public StreamConfiguration(@Value("${retryAttempts}") int retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    //@Bean
    //@StreamMessageConverter
    //public MessageConverter protobufMessageConverter() {
    //    return new ProtobufMessageConverter();
    //}

    @Bean
    @Profile("!test")
    public RabbitTransactionManager rabbitTransactionManager(ConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }

    @Bean
    public ListenerContainerCustomizer<AbstractMessageListenerContainer> listenerContainerCustomizer() {
        return (container, destinationName, group) ->
                container.addAfterReceivePostProcessors(retryHeaderMessagePostProcessor());
    }

    @Bean
    public MessagePostProcessor retryHeaderMessagePostProcessor() {
        return message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            if (messageProperties.getXDeathHeader() != null
                    && !messageProperties.getXDeathHeader().isEmpty()) {
                long retryCount = Optional.ofNullable(messageProperties.getXDeathHeader().get(0).get("count"))
                        .map(obj -> (Long) obj)
                        .orElse(0L);
                //minus one because we need to count first attempt which does not have x-death header
                messageProperties.getHeaders().put("retry-exceeded", retryCount >= retryAttempts - 1);
            } else {
                messageProperties.getHeaders().put("retry-exceeded", false);
            }
            return message;
        };
    }
}
