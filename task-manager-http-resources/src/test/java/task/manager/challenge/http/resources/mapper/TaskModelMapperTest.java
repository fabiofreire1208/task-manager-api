package task.manager.challenge.http.resources.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import task.manager.challenge.domain.model.Task;
import task.manager.challenge.domain.model.User;
import task.manager.challenge.http.resources.dto.TaskDto;
import task.manager.challenge.http.resources.mappers.TaskModelMapper;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskModelMapperTest {

    private final TaskModelMapper taskModelMapper = Mappers.getMapper(TaskModelMapper.class);

    @Test
    public void testFromTaskDtoToTask() {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(UUID.randomUUID());
        taskDto.setAssignedUserId(UUID.randomUUID());
        taskDto.setProjectId(UUID.randomUUID());
        taskDto.setWatcherIds(List.of(UUID.randomUUID(), UUID.randomUUID()));

        Task task = taskModelMapper.from(taskDto);

        assertNotNull(task);
        assertEquals(taskDto.getId(), task.getId());
        assertEquals(taskDto.getAssignedUserId(), task.getAssignedUser().getId());
        assertEquals(taskDto.getProjectId(), task.getProject().getId());
        assertNotNull(task.getWatchers());
        assertEquals(taskDto.getWatcherIds().size(), task.getWatchers().size());
    }

    @Test
    public void testMapWatcherIdsToUsers() {
        List<UUID> watcherIds = List.of(UUID.randomUUID(), UUID.randomUUID());

        List<User> users = taskModelMapper.mapWatcherIdsToUsers(watcherIds);

        assertNotNull(users);
        assertEquals(watcherIds.size(), users.size());
        assertEquals(watcherIds.get(0), users.get(0).getId());
        assertEquals(watcherIds.get(1), users.get(1).getId());
    }
}
