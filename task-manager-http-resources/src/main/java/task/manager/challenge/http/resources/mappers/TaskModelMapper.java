package task.manager.challenge.http.resources.mappers;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import task.manager.challenge.domain.model.Task;
import task.manager.challenge.domain.model.User;
import task.manager.challenge.http.resources.dto.TaskDto;
import task.manager.challenge.http.resources.dto.response.TaskResponseDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(builder = @Builder(disableBuilder = true), componentModel = "spring")
public interface TaskModelMapper {

    @Mapping(source = "assignedUserId", target = "assignedUser", qualifiedByName = "mapUserIdToUser")
    @Mapping(source = "projectId", target = "project.id")
    @Mapping(target = "watchers", expression = "java(mapWatcherIdsToUsers(source.getWatcherIds()))")
    Task from(TaskDto source);

    @Named("mapUserIdToUser")
    default User mapUserIdToUser(String userId) {
        if (userId == null) {
            return null; // Handle null case explicitly
        }
        User user = new User();
        user.setId(UUID.fromString(userId));
        return user;
    }

    TaskResponseDto from(Task source);

    default List<User> mapWatcherIdsToUsers(List<UUID> watcherIds) {
        return watcherIds != null ? watcherIds.stream().map(id -> {
            User user = new User();
            user.setId(id);
            return user;
        }).collect(Collectors.toList()) : null;
    }
}
