package task.manager.challenge.adapters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.exception.TaskNotFoundException;
import task.manager.challenge.core.exception.UserNotFoundException;
import task.manager.challenge.core.persistence.TaskRepositoryPort;
import task.manager.challenge.core.persistence.UserRepositoryPort;
import task.manager.challenge.domain.model.Task;
import task.manager.challenge.domain.model.User;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssignUserAdapterTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private AssignUserAdapter assignUserAdapter;

    @Test
    public void testProcess_SuccessfulAssignment() {
        UUID taskId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Context context = new Context();
        context.setData(taskId);
        context.setUserId(userId);

        Task task = new Task();
        task.setId(taskId);

        User user = new User();
        user.setId(userId);

        when(taskRepositoryPort.get(taskId)).thenReturn(Optional.of(task));
        when(userRepositoryPort.get(userId)).thenReturn(Optional.of(user));
        when(taskRepositoryPort.save(any(Task.class))).thenReturn(task);

        Void result = assignUserAdapter.process(context);

        assertNull(result);
        assertEquals(user, task.getAssignedUser());

        verify(taskRepositoryPort, times(1)).get(taskId);
        verify(userRepositoryPort, times(1)).get(userId);
        verify(taskRepositoryPort, times(1)).save(task);
    }

    @Test
    public void testProcess_TaskNotFound() {
        UUID taskId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Context context = new Context();
        context.setData(taskId);
        context.setUserId(userId);

        when(taskRepositoryPort.get(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> assignUserAdapter.process(context));

        verify(taskRepositoryPort, times(1)).get(taskId);
        verify(userRepositoryPort, never()).get(any());
        verify(taskRepositoryPort, never()).save(any());
    }

    @Test
    public void testProcess_UserNotFound() {
        UUID taskId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Context context = new Context();
        context.setData(taskId);
        context.setUserId(userId);

        Task task = new Task();
        task.setId(taskId);

        when(taskRepositoryPort.get(taskId)).thenReturn(Optional.of(task));
        when(userRepositoryPort.get(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> assignUserAdapter.process(context));

        verify(taskRepositoryPort, times(1)).get(taskId);
        verify(userRepositoryPort, times(1)).get(userId);
        verify(taskRepositoryPort, never()).save(any());
    }
}