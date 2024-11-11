package task.manager.challenge.core.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import task.manager.challenge.domain.enums.TaskPriority;
import task.manager.challenge.domain.enums.TaskStatus;
import task.manager.challenge.domain.model.Task;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepositoryPort {

    Task save(final Task obj);
    Optional<Task> get(final UUID id);
    Page<Task> filterTasks(UUID userId, TaskPriority priority, TaskStatus status, UUID projectId, Pageable pageable);

}
