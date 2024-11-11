package task.manager.challenge.http.resources.mappers;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import task.manager.challenge.domain.model.Project;
import task.manager.challenge.http.resources.dto.ProjectDto;

@Mapper(builder = @Builder(disableBuilder = true), componentModel = "spring")
public interface ProjectModelMapper {

    Project from(final ProjectDto source);
}
