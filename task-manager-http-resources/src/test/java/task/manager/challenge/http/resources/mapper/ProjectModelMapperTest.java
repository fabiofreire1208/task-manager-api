package task.manager.challenge.http.resources.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import task.manager.challenge.domain.model.Project;
import task.manager.challenge.http.resources.dto.ProjectDto;
import task.manager.challenge.http.resources.mappers.ProjectModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProjectModelMapperTest {

    private final ProjectModelMapper projectModelMapper = Mappers.getMapper(ProjectModelMapper.class);

    @Test
    public void testFromProjectDtoToProject() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Project Test");
        projectDto.setDescription("This is a test project.");

        Project project = projectModelMapper.from(projectDto);

        assertNotNull(project);
        assertEquals(projectDto.getName(), project.getName());
        assertEquals(projectDto.getDescription(), project.getDescription());
    }
}
