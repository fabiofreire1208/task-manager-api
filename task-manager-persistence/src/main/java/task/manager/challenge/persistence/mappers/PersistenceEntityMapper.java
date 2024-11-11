package task.manager.challenge.persistence.mappers;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import task.manager.challenge.domain.model.Device;
import task.manager.challenge.domain.model.Project;
import task.manager.challenge.domain.model.Task;
import task.manager.challenge.domain.model.User;
import task.manager.challenge.persistence.model.DeviceEntity;
import task.manager.challenge.persistence.model.ProjectEntity;
import task.manager.challenge.persistence.model.TaskEntity;
import task.manager.challenge.persistence.model.UserEntity;

@Mapper(builder = @Builder(disableBuilder = true), componentModel = "spring")
public interface PersistenceEntityMapper {

    User from(final UserEntity source);
    UserEntity from(final User source);

    Task from(final TaskEntity source);
    TaskEntity from(final Task source);

    Project from(final ProjectEntity source);
    ProjectEntity from(final Project source);

    Device from(final DeviceEntity source);
    DeviceEntity from(final Device source);
}
