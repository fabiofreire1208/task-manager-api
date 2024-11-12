package task.manager.challenge.adapters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.persistence.TaskRepositoryPort;
import task.manager.challenge.domain.enums.TaskPriority;
import task.manager.challenge.domain.enums.TaskStatus;
import task.manager.challenge.domain.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilterTaskAdapterTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @InjectMocks
    private FilterTaskAdapter filterTaskAdapter;

    @Test
    public void testProcess_SuccessfulFilter() {
        UUID userId = UUID.randomUUID();
        TaskPriority priority = TaskPriority.HIGH;
        TaskStatus status = TaskStatus.OPENED;
        UUID projectId = UUID.randomUUID();
        Pageable pageable = Pageable.unpaged();

        Task task1 = new Task();
        task1.setId(UUID.randomUUID());
        task1.setTitle("Task 1");
        Task task2 = new Task();
        task2.setId(UUID.randomUUID());
        task2.setTitle("Task 2");

        Page<Task> taskPage = new PageImpl<>(List.of(task1, task2));

        Context context = new Context();
        context.setUserId(userId);
        context.setPriority(priority);
        context.setStatus(status);
        context.setProjectId(projectId);
        context.setPageable(pageable);

        when(taskRepositoryPort.filterTasks(userId, priority, status, projectId, pageable)).thenReturn(taskPage);

        Optional<Page<Task>> result = filterTaskAdapter.process(context);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().getContent().size());
        assertEquals("Task 1", result.get().getContent().get(0).getTitle());
        assertEquals("Task 2", result.get().getContent().get(1).getTitle());

        verify(taskRepositoryPort, times(1)).filterTasks(userId, priority, status, projectId, pageable);
    }

    @Test
    public void testProcess_EmptyResult() {
        UUID userId = UUID.randomUUID();
        TaskPriority priority = TaskPriority.HIGH;
        TaskStatus status = TaskStatus.OPENED;
        UUID projectId = UUID.randomUUID();
        Pageable pageable = Pageable.unpaged();

        Page<Task> taskPage = new PageImpl<>(List.of()); // Empty page

        Context context = new Context();
        context.setUserId(userId);
        context.setPriority(priority);
        context.setStatus(status);
        context.setProjectId(projectId);
        context.setPageable(pageable);

        when(taskRepositoryPort.filterTasks(userId, priority, status, projectId, pageable)).thenReturn(taskPage);

        Optional<Page<Task>> result = filterTaskAdapter.process(context);

        assertTrue(result.isPresent());
        assertEquals(0, result.get().getContent().size());

        verify(taskRepositoryPort, times(1)).filterTasks(userId, priority, status, projectId, pageable);
    }

    @Test
    public void testProcess_NoTasksFound() {
        UUID userId = UUID.randomUUID();
        TaskPriority priority = TaskPriority.HIGH;
        TaskStatus status = TaskStatus.OPENED;
        UUID projectId = UUID.randomUUID();
        Pageable pageable = Pageable.unpaged();

        Page<Task> taskPage = new PageImpl<>(List.of());

        Context context = new Context();
        context.setUserId(userId);
        context.setPriority(priority);
        context.setStatus(status);
        context.setProjectId(projectId);
        context.setPageable(pageable);

        when(taskRepositoryPort.filterTasks(userId, priority, status, projectId, pageable)).thenReturn(taskPage);

        Optional<Page<Task>> result = filterTaskAdapter.process(context);

        assertTrue(result.isPresent());
        assertTrue(result.get().getContent().isEmpty());

        verify(taskRepositoryPort, times(1)).filterTasks(userId, priority, status, projectId, pageable);
    }
}
