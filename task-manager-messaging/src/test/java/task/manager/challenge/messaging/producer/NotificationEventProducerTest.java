package task.manager.challenge.messaging.producer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationEventProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private NotificationEventProducer notificationEventProducer;

    @Value("${spring.kafka.topic.task-update-notification}")
    private String notificationTopic;

    @Test
    public void testSendNotification() {
        String taskId = "12345";

        notificationEventProducer.send(taskId);

        verify(kafkaTemplate, times(1)).send(notificationTopic, taskId);
    }

    @Test
    public void testSendNotificationWhenExceptionOccurs() {
        String taskId = "12345";
        doThrow(new RuntimeException("Kafka failure")).when(kafkaTemplate).send(notificationTopic, taskId);

        notificationEventProducer.send(taskId);

        verify(kafkaTemplate, times(1)).send(notificationTopic, taskId);
    }
}
