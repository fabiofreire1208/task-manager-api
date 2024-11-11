package task.manager.challenge.messaging.consumer;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import task.manager.challenge.persistence.model.TaskEntity;
import task.manager.challenge.persistence.repository.TaskRepository;

import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class NotificationEventConsumer {
    private final TaskRepository taskRepository;

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.task-update-notification}"
    )
    @Transactional
    public void consumeTransactionEvent(String payload) {
        log.info("Receiving event {} from update-notification topic", payload);
        TaskEntity task = taskRepository.findByIdWithWatchersAndDevices(UUID.fromString(payload)).orElseThrow(
                () -> new RuntimeException("Task not found!")
        );

        task.getWatchers().stream()
                .filter(watcher -> watcher.getDevices() != null) // Check if devices are not null
                .flatMap(watcher -> watcher.getDevices().stream()) // Flatten the stream of devices
                .forEach(device -> log.info("Notifying {} about status change to {}", device.getDeviceName(), task.getStatus()));
    }
}
