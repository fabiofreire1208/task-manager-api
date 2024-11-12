package task.manager.challenge.http.resources;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import task.manager.challenge.core.business.*;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.exception.ProjectNotFoundException;
import task.manager.challenge.core.exception.UserNotFoundException;
import task.manager.challenge.domain.enums.TaskPriority;
import task.manager.challenge.domain.enums.TaskStatus;
import task.manager.challenge.domain.model.Project;
import task.manager.challenge.domain.model.Task;
import task.manager.challenge.domain.model.User;
import task.manager.challenge.http.resources.controllers.TaskController;
import task.manager.challenge.http.resources.dto.TaskDto;
import task.manager.challenge.http.resources.dto.TaskResponseDto;
import task.manager.challenge.http.resources.dto.TaskStatusDto;
import task.manager.challenge.http.resources.dto.UserAssignDto;
import task.manager.challenge.http.resources.mappers.TaskModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private CreateTaskPort createTaskPort;
    @Mock
    private AssignWatcherPort assignWatcherPort;
    @Mock
    private FilterTaskPort filterTaskPort;
    @Mock
    private ChangeTaskStatusPort changeTaskStatusPort;
    @Mock
    private AssignUserPort assignUserPort;
    @Mock
    private TaskModelMapper taskModelMapper;
    @InjectMocks
    private TaskController taskController;

    @Test
    public void addTask() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("My task");
        taskDto.setStatus(TaskStatus.OPENED);
        taskDto.setPriority(TaskPriority.NORMAL);
        taskDto.setDescription("My Description");
        taskDto.setDeadline(LocalDateTime.now());
        taskDto.setProjectId(UUID.randomUUID());
        taskDto.setAssignedUserId(UUID.randomUUID());

        Task task = new Task();
        task.setTitle("My task");
        task.setStatus("OPENED");
        task.setPriority("NORMAL");
        task.setDescription("My Description");
        task.setDeadline(LocalDateTime.now());

        User user = new User();
        user.setName("User Test");
        user.setEmail("user.test@example.com");

        Project project = new Project();
        project.setName("Project Test");
        project.setDescription("Description");

        task.setAssignedUser(user);
        task.setProject(project);

        when(taskModelMapper.from(taskDto)).thenReturn(task);
        when(createTaskPort.process(any(Context.class))).thenReturn(Optional.of(task));

        ResponseEntity<String> response = taskController.addTask(taskDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("The task was created successfully!", response.getBody());

        verify(taskModelMapper, times(1)).from(taskDto);
        verify(createTaskPort, times(1)).process(any(Context.class));
    }

    @Test
    public void addTaskWithUserNotFound() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("My task");
        taskDto.setStatus(TaskStatus.OPENED);
        taskDto.setPriority(TaskPriority.NORMAL);
        taskDto.setDescription("My Description");
        taskDto.setDeadline(LocalDateTime.now());
        taskDto.setProjectId(UUID.randomUUID());
        taskDto.setAssignedUserId(UUID.randomUUID());

        Task task = new Task();
        task.setTitle("My task");
        task.setStatus("OPENED");
        task.setPriority("NORMAL");
        task.setDescription("My Description");
        task.setDeadline(LocalDateTime.now());

        User user = new User();
        user.setName("User Test");
        user.setEmail("user.test@example.com");

        Project project = new Project();
        project.setName("Project Test");
        project.setDescription("Description");

        task.setAssignedUser(user);
        task.setProject(project);

        when(taskModelMapper.from(taskDto)).thenReturn(task);
        doThrow(new UserNotFoundException("User not found!")).when(createTaskPort).process(any(Context.class));

        RuntimeException exception = assertThrows(UserNotFoundException.class, () -> {
            taskController.addTask(taskDto);
        });

        assertEquals("User not found!", exception.getMessage());

        verify(taskModelMapper, times(1)).from(taskDto);
        verify(createTaskPort, times(1)).process(any(Context.class));
    }

    @Test
    public void addTaskWithProjectNotFound() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("My task");
        taskDto.setStatus(TaskStatus.OPENED);
        taskDto.setPriority(TaskPriority.NORMAL);
        taskDto.setDescription("My Description");
        taskDto.setDeadline(LocalDateTime.now());
        taskDto.setProjectId(UUID.randomUUID());
        taskDto.setAssignedUserId(UUID.randomUUID());

        Task task = new Task();
        task.setTitle("My task");
        task.setStatus("OPENED");
        task.setPriority("NORMAL");
        task.setDescription("My Description");
        task.setDeadline(LocalDateTime.now());

        User user = new User();
        user.setName("User Test");
        user.setEmail("user.test@example.com");

        Project project = new Project();
        project.setName("Project Test");
        project.setDescription("Description");

        task.setAssignedUser(user);
        task.setProject(project);

        when(taskModelMapper.from(taskDto)).thenReturn(task);
        doThrow(new ProjectNotFoundException("Project not found!")).when(createTaskPort).process(any(Context.class));

        RuntimeException exception = assertThrows(ProjectNotFoundException.class, () -> {
            taskController.addTask(taskDto);
        });

        assertEquals("Project not found!", exception.getMessage());

        verify(taskModelMapper, times(1)).from(taskDto);
        verify(createTaskPort, times(1)).process(any(Context.class));
    }

    @Test
    public void addWatchers() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(UUID.randomUUID());
        taskDto.setTitle("My task");
        taskDto.setStatus(TaskStatus.OPENED);
        taskDto.setPriority(TaskPriority.NORMAL);
        taskDto.setDescription("My Description");
        taskDto.setDeadline(LocalDateTime.now());
        taskDto.setProjectId(UUID.randomUUID());
        taskDto.setAssignedUserId(UUID.randomUUID());

        List<UUID> watchersList = new ArrayList<>();
        watchersList.add(UUID.randomUUID());

        taskDto.setWatcherIds(watchersList);

        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle("My task");
        task.setStatus("OPENED");
        task.setPriority("NORMAL");
        task.setDescription("My Description");
        task.setDeadline(LocalDateTime.now());

        User user = new User();
        user.setName("User Test");
        user.setEmail("user.test@example.com");

        Project project = new Project();
        project.setName("Project Test");
        project.setDescription("Description");

        User watcher = new User();
        watcher.setId(UUID.randomUUID());
        watcher.setName("watcher Test");
        watcher.setEmail("user.test@example.com");

        List<User> watchersModelList = new ArrayList<>();
        watchersModelList.add(watcher);

        task.setAssignedUser(user);
        task.setProject(project);
        task.setWatchers(watchersModelList);

        when(taskModelMapper.from(taskDto)).thenReturn(task);
        when(assignWatcherPort.process(any(Context.class))).thenReturn(Optional.of(task));

        ResponseEntity<String> response = taskController.addWatchersToTask(taskDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("The watchers list was updated successfully!", response.getBody());

        verify(taskModelMapper, times(1)).from(taskDto);
        verify(assignWatcherPort, times(1)).process(any(Context.class));
    }

    @Test
    public void filterTasks() throws Exception {
        UUID userId = UUID.randomUUID();
        TaskPriority priority = TaskPriority.HIGH;
        TaskStatus status = TaskStatus.IN_PROGRESS;
        UUID projectId = UUID.randomUUID();
        PageRequest pageable = PageRequest.of(0, 10);

        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle("My task");
        task.setStatus("OPENED");
        task.setPriority("NORMAL");
        task.setDescription("My Description");
        task.setDeadline(LocalDateTime.now());

        Page<Task> taskPage = new PageImpl<Task>(List.of(task), pageable, 2);

        when(filterTaskPort.process(any(Context.class))).thenReturn(Optional.of(taskPage));

        ResponseEntity<List<TaskResponseDto>> response = taskController.filterTasks(userId, priority, status, projectId, pageable);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());

        verify(filterTaskPort, times(1)).process(any(Context.class));
        verify(taskModelMapper, times(1)).from(task);
    }

    @Test
    public void testUpdateStatus() {
        UUID taskId = UUID.randomUUID();
        TaskStatusDto statusDto = new TaskStatusDto(TaskStatus.COMPLETED);

        doNothing().when(changeTaskStatusPort).process(any(Context.class));

        ResponseEntity<String> response = taskController.updateStatus(taskId, statusDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Task status was updated successfully!", response.getBody());

        verify(changeTaskStatusPort, times(1)).process(any(Context.class));
    }

    @Test
    public void testUpdateTaskUserAssigned() {
        UUID taskId = UUID.randomUUID();
        UserAssignDto userAssignDto = new UserAssignDto(UUID.randomUUID());

        doNothing().when(assignUserPort).process(any(Context.class));

        ResponseEntity<String> response = taskController.updateTaskUserAssigned(taskId, userAssignDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User assigned to the task was updated successfully!", response.getBody());

        verify(assignUserPort, times(1)).process(any(Context.class));
    }

}
