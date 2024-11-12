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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssignWatcherAdapterTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private AssignWatcherAdapter assignWatcherAdapter;

    @Test
    public void testProcess_SuccessfulWatcherAssignment() {
        UUID taskId = UUID.randomUUID();
        UUID watcherId1 = UUID.randomUUID();
        UUID watcherId2 = UUID.randomUUID();

        User watcher1 = new User();
        watcher1.setId(watcherId1);

        User watcher2 = new User();
        watcher2.setId(watcherId2);

        Task task = new Task();
        task.setId(taskId);
        task.setWatchers(new ArrayList<>());

        Task data = new Task();
        data.setId(taskId);
        data.setWatchers(Arrays.asList(watcher1, watcher2));

        Context context = new Context();
        context.setData(data);

        when(taskRepositoryPort.get(taskId)).thenReturn(Optional.of(task));
        when(userRepositoryPort.get(watcherId1)).thenReturn(Optional.of(watcher1));
        when(userRepositoryPort.get(watcherId2)).thenReturn(Optional.of(watcher2));
        when(taskRepositoryPort.save(task)).thenReturn(task);

        Optional<Task> result = assignWatcherAdapter.process(context);

        assertTrue(result.isPresent());
        assertEquals(2, task.getWatchers().size());
        assertTrue(task.getWatchers().contains(watcher1));
        assertTrue(task.getWatchers().contains(watcher2));

        verify(taskRepositoryPort, times(1)).get(taskId);
        verify(userRepositoryPort, times(1)).get(watcherId1);
        verify(userRepositoryPort, times(1)).get(watcherId2);
        verify(taskRepositoryPort, times(1)).save(task);
    }

    @Test
    public void testProcess_TaskNotFound() {
        UUID taskId = UUID.randomUUID();
        UUID watcherId = UUID.randomUUID();

        User watcher = new User();
        watcher.setId(watcherId);

        Task data = new Task();
        data.setId(taskId);
        data.setWatchers(Collections.singletonList(watcher));

        Context context = new Context();
        context.setData(data);

        when(taskRepositoryPort.get(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> assignWatcherAdapter.process(context));

        verify(taskRepositoryPort, times(1)).get(taskId);
        verify(userRepositoryPort, never()).get(any());
        verify(taskRepositoryPort, never()).save(any());
    }

    @Test
    public void testProcess_UserNotFound() {
        UUID taskId = UUID.randomUUID();
        UUID watcherId = UUID.randomUUID();

        User watcher = new User();
        watcher.setId(watcherId);

        Task task = new Task();
        task.setId(taskId);
        task.setWatchers(new ArrayList<>());

        Task data = new Task();
        data.setId(taskId);
        data.setWatchers(Collections.singletonList(watcher));

        Context context = new Context();
        context.setData(data);

        when(taskRepositoryPort.get(taskId)).thenReturn(Optional.of(task));
        when(userRepositoryPort.get(watcherId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> assignWatcherAdapter.process(context));

        verify(taskRepositoryPort, times(1)).get(taskId);
        verify(userRepositoryPort, times(1)).get(watcherId);
        verify(taskRepositoryPort, never()).save(any());
    }
}
