package task.manager.challenge.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.manager.challenge.core.business.AssignUserPort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.exception.TaskNotFoundException;
import task.manager.challenge.core.exception.UserNotFoundException;
import task.manager.challenge.core.persistence.TaskRepositoryPort;
import task.manager.challenge.core.persistence.UserRepositoryPort;
import task.manager.challenge.domain.model.Task;
import task.manager.challenge.domain.model.User;

import java.util.UUID;

@Service
public class AssignUserAdapter implements AssignUserPort {
    private final TaskRepositoryPort taskRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Autowired
    public AssignUserAdapter(TaskRepositoryPort taskRepositoryPort, UserRepositoryPort userRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Void process(Context context) {
        final UUID data = context.getData(UUID.class);
        final UUID userId = context.getUserId();

        final Task task = taskRepositoryPort.get(data).orElseThrow(
                () -> new TaskNotFoundException("Task not found!")
        );

        final User user = userRepositoryPort.get(userId).orElseThrow(
                () -> new UserNotFoundException("User not found!")
        );

        task.setAssignedUser(user);
        taskRepositoryPort.save(task);
        return null;
    }
}
