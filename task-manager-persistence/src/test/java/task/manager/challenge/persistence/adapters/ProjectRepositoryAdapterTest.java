package task.manager.challenge.persistence.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.manager.challenge.domain.model.Project;
import task.manager.challenge.persistence.mappers.PersistenceEntityMapper;
import task.manager.challenge.persistence.model.ProjectEntity;
import task.manager.challenge.persistence.repository.ProjectRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectRepositoryAdapterTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private PersistenceEntityMapper mapper;

    @InjectMocks
    private ProjectRepositoryAdapter projectRepositoryAdapter;

    private Project project;
    private ProjectEntity projectEntity;
    private UUID projectId;

    @BeforeEach
    public void setUp() {
        projectId = UUID.randomUUID();
        project = new Project();
        project.setId(projectId);
        project.setName("Test Project");

        projectEntity = new ProjectEntity();
        projectEntity.setId(projectId);
        projectEntity.setName("Test Project");
    }

    @Test
    public void testSave() {
        when(mapper.from(project)).thenReturn(projectEntity);
        when(projectRepository.save(projectEntity)).thenReturn(projectEntity);
        when(mapper.from(projectEntity)).thenReturn(project);

        Project savedProject = projectRepositoryAdapter.save(project);

        assertNotNull(savedProject);
        assertEquals(project.getId(), savedProject.getId());
        verify(projectRepository, times(1)).save(projectEntity);
        verify(mapper, times(1)).from(project);
        verify(mapper, times(1)).from(projectEntity);
    }

    @Test
    public void testGetById_Found() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projectEntity));
        when(mapper.from(projectEntity)).thenReturn(project);

        Optional<Project> foundProject = projectRepositoryAdapter.get(projectId);

        assertTrue(foundProject.isPresent());
        assertEquals(project.getId(), foundProject.get().getId());
        verify(projectRepository, times(1)).findById(projectId);
        verify(mapper, times(1)).from(projectEntity);
    }

    @Test
    public void testGetById_NotFound() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        Optional<Project> foundProject = projectRepositoryAdapter.get(projectId);

        assertFalse(foundProject.isPresent());
        verify(projectRepository, times(1)).findById(projectId);
        verify(mapper, never()).from(any(ProjectEntity.class));
    }
}
