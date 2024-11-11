package task.manager.challenge.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.manager.challenge.core.business.CreateProjectPort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.persistence.ProjectRepositoryPort;
import task.manager.challenge.domain.model.Project;

import java.util.Optional;

@Service
public class CreateProjectAdapter implements CreateProjectPort {
    private final ProjectRepositoryPort projectRepositoryPort;

    @Autowired
    public CreateProjectAdapter(ProjectRepositoryPort projectRepositoryPort) {
        this.projectRepositoryPort = projectRepositoryPort;
    }

    @Override
    public Optional<Project> process(Context context) {
        final Project data = context.getData(Project.class);
        return Optional.ofNullable(projectRepositoryPort.save(data));
    }
}
