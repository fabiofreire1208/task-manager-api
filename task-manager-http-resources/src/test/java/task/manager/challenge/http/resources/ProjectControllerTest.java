package task.manager.challenge.http.resources;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import task.manager.challenge.core.business.CreateProjectPort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.domain.model.Project;
import task.manager.challenge.http.resources.controllers.ProjectController;
import task.manager.challenge.http.resources.dto.ProjectDto;
import task.manager.challenge.http.resources.mappers.ProjectModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest {

    @Mock
    private CreateProjectPort createProjectPort;

    @Mock
    private ProjectModelMapper projectModelMapper;

    @InjectMocks
    private ProjectController projectController;

    @Test
    public void addProject() throws Exception {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Project Test");
        projectDto.setDescription("Description");

        Project project = new Project();
        project.setName("Project Test");
        project.setDescription("Description");

        when(projectModelMapper.from(projectDto)).thenReturn(project);
        when(createProjectPort.process(any(Context.class))).thenReturn(Optional.of(project));

        ResponseEntity<String> response = projectController.addProject(projectDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("The project was created successfully!", response.getBody());

        verify(projectModelMapper, times(1)).from(projectDto);
        verify(createProjectPort, times(1)).process(any(Context.class));
    }
}
