package task.manager.challenge.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import task.manager.challenge.core.business.FilterTaskPort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.persistence.TaskRepositoryPort;
import task.manager.challenge.domain.enums.TaskPriority;
import task.manager.challenge.domain.enums.TaskStatus;
import task.manager.challenge.domain.model.Task;

import java.util.Optional;
import java.util.UUID;

@Service
public class FilterTaskAdapter implements FilterTaskPort {
    private final TaskRepositoryPort taskRepositoryPort;

    @Autowired
    public FilterTaskAdapter(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }

    @Override
    public Optional<Page<Task>> process(Context context) {
        final UUID userId = context.getUserId();
        final TaskPriority priority = context.getPriority();
        final TaskStatus status = context.getStatus();
        final UUID projectId = context.getProjectId();
        Pageable pageable = context.getPageable();

        Page<Task> taskPage = taskRepositoryPort.filterTasks(userId, priority, status, projectId, pageable);

        return Optional.of(taskPage);
    }
}
