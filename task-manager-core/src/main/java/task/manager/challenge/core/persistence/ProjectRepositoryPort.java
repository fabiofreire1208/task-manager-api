package task.manager.challenge.core.persistence;

import task.manager.challenge.domain.model.Project;
import task.manager.challenge.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepositoryPort {
    Project save(final Project obj);
    Optional<Project> get(final UUID id);
    List<Optional<Project>> findAllProjects();
}
