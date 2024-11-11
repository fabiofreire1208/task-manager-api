package task.manager.challenge.persistence.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import task.manager.challenge.core.persistence.TaskRepositoryPort;
import task.manager.challenge.domain.enums.TaskPriority;
import task.manager.challenge.domain.enums.TaskStatus;
import task.manager.challenge.domain.model.Task;
import task.manager.challenge.persistence.mappers.PersistenceEntityMapper;
import task.manager.challenge.persistence.repository.TaskRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TaskRepositoryAdapter implements TaskRepositoryPort {
    private final TaskRepository taskRepository;
    private final PersistenceEntityMapper mapper;

    @Autowired
    public TaskRepositoryAdapter(TaskRepository taskRepository, PersistenceEntityMapper mapper) {
        this.taskRepository = taskRepository;
        this.mapper = mapper;
    }

    @Override
    public Task save(Task obj) {
        return mapper.from(taskRepository.save(mapper.from(obj)));
    }

    @Override
    public Optional<Task> get(UUID id) {
        Task task = taskRepository.findById(id)
                .map(mapper::from)
                .orElse(null);

        return Objects.nonNull(task) ? Optional.of(task) : Optional.empty();
    }

    @Override
    public Page<Task> filterTasks(UUID userId, TaskPriority priority, TaskStatus status, UUID projectId, Pageable pageable) {
        return taskRepository.filterTasks(userId, priority, status, projectId, pageable)
                .map(mapper::from);
    }
}
