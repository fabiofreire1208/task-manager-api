package task.manager.challenge.messaging.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import task.manager.challenge.core.messaging.NotificationEventProducerPort;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventProducer implements NotificationEventProducerPort {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.task-update-notification}")
    private String notificationTopic;
    @Override
    public void send(String taskId) {
        try {
            log.info("Sending event to topic {} with data {}", notificationTopic, taskId);
            kafkaTemplate.send(notificationTopic, taskId);
        } catch (Exception ex) {
            log.error("Error trying to send data to topic {} with data {}", notificationTopic, taskId, ex);
        }

    }
}
