package task.manager.challenge.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.manager.challenge.core.business.AssignWatcherPort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.persistence.ProjectRepositoryPort;
import task.manager.challenge.core.persistence.TaskRepositoryPort;
import task.manager.challenge.core.persistence.UserRepositoryPort;
import task.manager.challenge.domain.model.Task;
import task.manager.challenge.domain.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class AssignWatcherAdapter implements AssignWatcherPort {
    private final TaskRepositoryPort taskRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Autowired
    public AssignWatcherAdapter(TaskRepositoryPort taskRepositoryPort, UserRepositoryPort userRepositoryPort, ProjectRepositoryPort projectRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Optional<Task> process(Context context) {
        final Task data = context.getData(Task.class);
        final Task task = taskRepositoryPort.get(data.getId()).orElseThrow(
                () -> new RuntimeException("Task not found!")
        );

        List<User> users = data.getWatchers().stream()
                .map(user -> userRepositoryPort.get(user.getId())
                        .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + user.getId())))
                .toList();

        task.getWatchers().addAll(users);
        return Optional.ofNullable(taskRepositoryPort.save(task));
    }
}
