package ing;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class IngMessageHandler {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(IngMessageHandler.class);

    @Transactional
    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    public Message<String> processMessage(Message<String> message) {
        String input = message.getPayload();
        log.info("got message payload: {}", input);

        return MessageBuilder
            .withPayload(input + "done")
            .setHeader(MessageHeaders.CONTENT_TYPE, "application/x-protobuf")
            .setHeader("routingKey", "???")
            .build();
    }
}
