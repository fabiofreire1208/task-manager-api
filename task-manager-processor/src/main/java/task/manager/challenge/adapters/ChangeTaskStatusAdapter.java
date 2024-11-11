package task.manager.challenge.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.manager.challenge.core.business.ChangeTaskStatusPort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.exception.TaskNotFoundException;
import task.manager.challenge.core.messaging.NotificationEventProducerPort;
import task.manager.challenge.core.persistence.TaskRepositoryPort;
import task.manager.challenge.domain.enums.TaskStatus;
import task.manager.challenge.domain.model.Task;
import task.manager.challenge.persistence.mappers.PersistenceEntityMapper;
import task.manager.challenge.persistence.model.TaskEntity;

import java.util.UUID;

@Service
public class ChangeTaskStatusAdapter implements ChangeTaskStatusPort {
    private final TaskRepositoryPort taskRepositoryPort;
    private final NotificationEventProducerPort notificationEventProducerPort;
    private final PersistenceEntityMapper mapper;

    @Autowired
    public ChangeTaskStatusAdapter(TaskRepositoryPort taskRepositoryPort, NotificationEventProducerPort notificationEventProducerPort, PersistenceEntityMapper mapper) {
        this.taskRepositoryPort = taskRepositoryPort;
        this.notificationEventProducerPort = notificationEventProducerPort;
        this.mapper = mapper;
    }

    @Override
    public Void process(Context context) {
        final UUID data = context.getData(UUID.class);
        final Task task = taskRepositoryPort.get(data).orElseThrow(
                () -> new TaskNotFoundException("Task not found!")
        );

        final TaskStatus status = context.getStatus();
        task.setStatus(status.name());

        taskRepositoryPort.save(task);

        notifyWatchers(mapper.from(task));
        return null;
    }

    private void notifyWatchers(TaskEntity task) {
        notificationEventProducerPort.send(task.getId().toString());
    }
}
