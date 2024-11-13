package task.manager.challenge.http.resources.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task.manager.challenge.core.business.*;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.domain.enums.TaskPriority;
import task.manager.challenge.domain.enums.TaskStatus;
import task.manager.challenge.domain.model.Task;
import task.manager.challenge.http.resources.dto.TaskDto;
import task.manager.challenge.http.resources.dto.response.TaskResponseDto;
import task.manager.challenge.http.resources.dto.TaskStatusDto;
import task.manager.challenge.http.resources.dto.UserAssignDto;
import task.manager.challenge.http.resources.mappers.TaskModelMapper;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/tasks")
public class TaskController {
    private final CreateTaskPort createTaskPort;
    private final AssignWatcherPort assignWatcherPort;
    private final FilterTaskPort filterTaskPort;
    private final ChangeTaskStatusPort changeTaskStatusPort;
    private final AssignUserPort assignUserPort;
    private final TaskModelMapper taskModelMapper;

    @Autowired
    public TaskController(CreateTaskPort createTaskPort, AssignWatcherPort assignWatcherPort, FilterTaskPort filterTaskPort, ChangeTaskStatusPort changeTaskStatusPort, AssignUserPort assignUserPort, TaskModelMapper taskModelMapper) {
        this.createTaskPort = createTaskPort;
        this.assignWatcherPort = assignWatcherPort;
        this.filterTaskPort = filterTaskPort;
        this.changeTaskStatusPort = changeTaskStatusPort;
        this.assignUserPort = assignUserPort;
        this.taskModelMapper = taskModelMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTask(@RequestBody TaskDto taskRequest) {
        final Task task = taskModelMapper.from(taskRequest);

        final Context context = new Context();
        context.setData(task);

        createTaskPort.process(context);

        return ResponseEntity.ok("The task was created successfully!");
    }

    @PostMapping("add/watchers/list")
    public ResponseEntity<String> addWatchersToTask(@RequestBody TaskDto taskRequest) {
        final Task task = taskModelMapper.from(taskRequest);

        final Context context = new Context();
        context.setData(task);

        assignWatcherPort.process(context);

        return ResponseEntity.ok("The watchers list was updated successfully!");
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TaskResponseDto>> filterTasks(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) UUID projectId,
            Pageable pageable) {

        final Context context = new Context();
        context.setUserId(userId);
        context.setPriority(priority);
        context.setStatus(status);
        context.setProjectId(projectId);
        context.setPageable(pageable);

        Optional<Page<Task>> result = filterTaskPort.process(context);

        List<TaskResponseDto> taskDtos = result
                .map(tasksPage -> tasksPage.getContent().stream()
                        .map(taskModelMapper::from)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        return ResponseEntity.ok(taskDtos);
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<String> updateStatus(@PathVariable UUID taskId, @RequestBody TaskStatusDto status) {
        final Context context = new Context();
        context.setData(taskId);
        context.setStatus(status.getStatus());

        changeTaskStatusPort.process(context);

        return ResponseEntity.ok("Task status was updated successfully!");
    }

    @PutMapping("/{taskId}/user/assign")
    public ResponseEntity<String> updateTaskUserAssigned(@PathVariable UUID taskId, @RequestBody UserAssignDto userId) {
        final Context context = new Context();
        context.setData(taskId);
        context.setUserId(userId.getUserId());

        assignUserPort.process(context);

        return ResponseEntity.ok("User assigned to the task was updated successfully!");
    }

}
