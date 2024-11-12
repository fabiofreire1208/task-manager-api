package task.manager.challenge.adapters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.exception.TaskNotFoundException;
import task.manager.challenge.core.messaging.NotificationEventProducerPort;
import task.manager.challenge.core.persistence.TaskRepositoryPort;
import task.manager.challenge.domain.enums.TaskStatus;
import task.manager.challenge.domain.model.Task;
import task.manager.challenge.persistence.mappers.PersistenceEntityMapper;
import task.manager.challenge.persistence.model.TaskEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChangeTaskStatusAdapterTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @Mock
    private NotificationEventProducerPort notificationEventProducerPort;

    @Mock
    private PersistenceEntityMapper mapper;

    @InjectMocks
    private ChangeTaskStatusAdapter changeTaskStatusAdapter;

    @Test
    public void testProcess_SuccessfulStatusChange() {
        UUID taskId = UUID.randomUUID();
        TaskStatus newStatus = TaskStatus.IN_PROGRESS;

        Task task = new Task();
        task.setId(taskId);
        task.setStatus(TaskStatus.OPENED.name());

        Context context = new Context();
        context.setData(taskId);
        context.setStatus(newStatus);

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(taskId);

        when(taskRepositoryPort.get(taskId)).thenReturn(Optional.of(task));
        when(mapper.from(task)).thenReturn(taskEntity);
        when(taskRepositoryPort.save(task)).thenReturn(task);

        Void result = changeTaskStatusAdapter.process(context);

        assertNull(result);
        assertEquals(newStatus.name(), task.getStatus());

        verify(taskRepositoryPort, times(1)).get(taskId);
        verify(taskRepositoryPort, times(1)).save(task);
        verify(notificationEventProducerPort, times(1)).send(taskId.toString());
    }

    @Test
    public void testProcess_TaskNotFound() {
        UUID taskId = UUID.randomUUID();
        TaskStatus newStatus = TaskStatus.IN_PROGRESS;

        Context context = new Context();
        context.setData(taskId);
        context.setStatus(newStatus);

        when(taskRepositoryPort.get(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> changeTaskStatusAdapter.process(context));

        verify(taskRepositoryPort, times(1)).get(taskId);
        verify(taskRepositoryPort, never()).save(any());
        verify(notificationEventProducerPort, never()).send(any());
    }
}
