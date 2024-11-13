package task.manager.challenge.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.manager.challenge.core.business.ListProjectPort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.persistence.ProjectRepositoryPort;
import task.manager.challenge.domain.model.Project;

import java.util.List;
import java.util.Optional;

@Service
public class ListProjectAdapter implements ListProjectPort {
    private final ProjectRepositoryPort projectRepositoryPort;

    @Autowired
    public ListProjectAdapter(ProjectRepositoryPort projectRepositoryPort) {
        this.projectRepositoryPort = projectRepositoryPort;
    }

    @Override
    public List<Optional<Project>> process(Context context) {
        return projectRepositoryPort.findAllProjects();
    }
}
