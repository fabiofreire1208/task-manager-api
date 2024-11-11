package task.manager.challenge.http.resources.mappers;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import task.manager.challenge.domain.model.Task;
import task.manager.challenge.domain.model.User;
import task.manager.challenge.http.resources.dto.TaskDto;
import task.manager.challenge.http.resources.dto.TaskResponseDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(builder = @Builder(disableBuilder = true), componentModel = "spring")
public interface TaskModelMapper {

    @Mapping(source = "assignedUserId", target = "assignedUser.id")
    @Mapping(source = "projectId", target = "project.id")
    @Mapping(target = "watchers", expression = "java(mapWatcherIdsToUsers(source.getWatcherIds()))")
    Task from(TaskDto source);

    TaskResponseDto from(Task source);

    default List<User> mapWatcherIdsToUsers(List<UUID> watcherIds) {
        // This should be replaced by the actual logic to fetch User objects from IDs
        return watcherIds != null ? watcherIds.stream().map(id -> {
            User user = new User();
            user.setId(id);
            return user;
        }).collect(Collectors.toList()) : null;
    }
}
