package task.manager.challenge.messaging.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class NotificationEventConsumer {

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.task-update-notification}"
    )
    public void consumeTransactionEvent(String payload) {
        log.info("Receiving event {} from update-notification topic", payload);
    }
}
