package task.manager.challenge.persistence.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import task.manager.challenge.core.persistence.ProjectRepositoryPort;
import task.manager.challenge.domain.model.Project;
import task.manager.challenge.persistence.mappers.PersistenceEntityMapper;
import task.manager.challenge.persistence.repository.ProjectRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProjectRepositoryAdapter implements ProjectRepositoryPort {
    private final ProjectRepository projectRepository;
    private final PersistenceEntityMapper mapper;

    @Autowired
    public ProjectRepositoryAdapter(ProjectRepository projectRepository, PersistenceEntityMapper mapper) {
        this.projectRepository = projectRepository;
        this.mapper = mapper;
    }

    @Override
    public Project save(Project obj) {
        return mapper.from(projectRepository.save(mapper.from(obj)));
    }

    @Override
    public Optional<Project> get(UUID id) {
        Project project = projectRepository.findById(id)
                .map(mapper::from)
                .orElse(null);

        return Objects.nonNull(project) ? Optional.of(project) : Optional.empty();
    }
}
