package task.manager.challenge.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.manager.challenge.core.business.CreateTaskPort;
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

@Service
public class CreateTaskAdapter implements CreateTaskPort {
    private final TaskRepositoryPort taskRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final ProjectRepositoryPort projectRepositoryPort;

    @Autowired
    public CreateTaskAdapter(TaskRepositoryPort taskRepositoryPort, UserRepositoryPort userRepositoryPort, ProjectRepositoryPort projectRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.projectRepositoryPort = projectRepositoryPort;
    }

    @Override
    public Optional<Task> process(Context context) {
        final Task data = context.getData(Task.class);
        if(Optional.ofNullable(data.getAssignedUser()).isPresent()) {
            final User userToAssign = userRepositoryPort.get(data.getAssignedUser().getId()).orElseThrow(
                    () -> new UserNotFoundException("User not found!")
            );
        }

        final Project project = projectRepositoryPort.get(data.getProject().getId()).orElseThrow(
                () -> new ProjectNotFoundException("Project not found!")
        );

        return Optional.ofNullable(taskRepositoryPort.save(data));
    }
}
