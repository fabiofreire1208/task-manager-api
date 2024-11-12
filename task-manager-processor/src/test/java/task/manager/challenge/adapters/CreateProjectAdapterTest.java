package task.manager.challenge.adapters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.persistence.ProjectRepositoryPort;
import task.manager.challenge.domain.model.Project;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateProjectAdapterTest {

    @Mock
    private ProjectRepositoryPort projectRepositoryPort;

    @InjectMocks
    private CreateProjectAdapter createProjectAdapter;

    @Test
    public void testProcess_SuccessfulProjectCreation() {
        Project project = new Project();
        project.setName("New Project");

        Context context = new Context();
        context.setData(project);

        when(projectRepositoryPort.save(project)).thenReturn(project);

        Optional<Project> result = createProjectAdapter.process(context);

        assertTrue(result.isPresent()); // Verify that the project was saved successfully
        assertEquals(project, result.get()); // Verify that the returned project is the same as the input project

        verify(projectRepositoryPort, times(1)).save(project);
    }
}
