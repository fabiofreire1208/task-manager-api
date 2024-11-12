package task.manager.challenge.messaging.consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.manager.challenge.persistence.model.TaskEntity;
import task.manager.challenge.persistence.model.UserEntity;
import task.manager.challenge.persistence.repository.TaskRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationEventConsumerTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private NotificationEventConsumer notificationEventConsumer;

    @Test
    public void testConsumeTransactionEvent_Success() {
        UUID taskId = UUID.randomUUID();
        String payload = taskId.toString();
        TaskEntity taskEntity = new TaskEntity();

        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());

        Set<UserEntity> watchers = new HashSet<>();
        watchers.add(userEntity);

        taskEntity.setWatchers(watchers);

        when(taskRepository.findByIdWithWatchersAndDevices(taskId)).thenReturn(Optional.of(taskEntity));

        notificationEventConsumer.consumeTransactionEvent(payload);

        verify(taskRepository, times(1)).findByIdWithWatchersAndDevices(UUID.fromString(payload));

        assertEquals(1, taskEntity.getWatchers().size());
    }

    @Test
    public void testConsumeTransactionEvent_TaskNotFound() {
        UUID taskId = UUID.randomUUID();
        String payload = taskId.toString();
        TaskEntity taskEntity;

        when(taskRepository.findByIdWithWatchersAndDevices(any(UUID.class))).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationEventConsumer.consumeTransactionEvent(payload);
        });

        assertEquals("Task not found!", exception.getMessage());
    }
}
