package task.manager.challenge.adapters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.exception.ProjectNotFoundException;
import task.manager.challenge.core.exception.UserNotFoundException;
import task.manager.challenge.core.persistence.ProjectRepositoryPort;
import task.manager.challenge.core.persistence.TaskRepositoryPort;
import task.manager.challenge.core.persistence.UserRepositoryPort;
import task.manager.challenge.domain.model.Project;
import task.manager.challenge.domain.model.Task;
import task.manager.challenge.domain.model.User;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateTaskAdapterTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private ProjectRepositoryPort projectRepositoryPort;

    @InjectMocks
    private CreateTaskAdapter createTaskAdapter;

    @Test
    public void testProcess_SuccessfulTaskCreation() {
        Task task = new Task();
        task.setTitle("New Task");

        User user = new User();
        user.setId(UUID.randomUUID());
        task.setAssignedUser(user);

        Project project = new Project();
        project.setId(UUID.randomUUID());
        task.setProject(project);

        Context context = new Context();
        context.setData(task);

        when(userRepositoryPort.get(user.getId())).thenReturn(Optional.of(user));
        when(projectRepositoryPort.get(project.getId())).thenReturn(Optional.of(project));
        when(taskRepositoryPort.save(task)).thenReturn(task);

        Optional<Task> result = createTaskAdapter.process(context);

        assertTrue(result.isPresent());
        assertEquals(task, result.get());

        verify(userRepositoryPort, times(1)).get(user.getId());
        verify(projectRepositoryPort, times(1)).get(project.getId());
        verify(taskRepositoryPort, times(1)).save(task);
    }

    @Test
    public void testProcess_UserNotFound() {
        Task task = new Task();
        task.setTitle("New Task");

        User user = new User();
        user.setId(UUID.randomUUID());
        task.setAssignedUser(user);

        Project project = new Project();
        project.setId(UUID.randomUUID());
        task.setProject(project);

        Context context = new Context();
        context.setData(task);

        when(userRepositoryPort.get(user.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> createTaskAdapter.process(context));

        verify(userRepositoryPort, times(1)).get(user.getId());
        verify(projectRepositoryPort, never()).get(any());
        verify(taskRepositoryPort, never()).save(any());
    }

    @Test
    public void testProcess_ProjectNotFound() {
        Task task = new Task();
        task.setTitle("New Task");

        User user = new User();
        user.setId(UUID.randomUUID());
        task.setAssignedUser(user);

        Project project = new Project();
        project.setId(UUID.randomUUID());
        task.setProject(project);

        Context context = new Context();
        context.setData(task);

        when(userRepositoryPort.get(user.getId())).thenReturn(Optional.of(user));
        when(projectRepositoryPort.get(project.getId())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> createTaskAdapter.process(context));

        verify(userRepositoryPort, times(1)).get(user.getId());
        verify(projectRepositoryPort, times(1)).get(project.getId());
        verify(taskRepositoryPort, never()).save(any());
    }
}
