package task.manager.challenge.persistence.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import task.manager.challenge.domain.enums.TaskPriority;
import task.manager.challenge.domain.enums.TaskStatus;
import task.manager.challenge.domain.model.Task;
import task.manager.challenge.persistence.mappers.PersistenceEntityMapper;
import task.manager.challenge.persistence.model.TaskEntity;
import task.manager.challenge.persistence.repository.TaskRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskRepositoryAdapterTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private PersistenceEntityMapper mapper;

    @InjectMocks
    private TaskRepositoryAdapter taskRepositoryAdapter;

    private Task task;
    private TaskEntity taskEntity;
    private UUID taskId;

    @BeforeEach
    public void setUp() {
        taskId = UUID.randomUUID();
        task = new Task();
        task.setId(taskId);
        task.setTitle("Test Task");

        taskEntity = new TaskEntity();
        taskEntity.setId(taskId);
        taskEntity.setTitle("Test Task");
    }

    @Test
    public void testSave() {
        when(mapper.from(task)).thenReturn(taskEntity);
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(mapper.from(taskEntity)).thenReturn(task);

        Task savedTask = taskRepositoryAdapter.save(task);

        assertNotNull(savedTask);
        assertEquals(task.getId(), savedTask.getId());
        verify(taskRepository, times(1)).save(taskEntity);
        verify(mapper, times(1)).from(task);
        verify(mapper, times(1)).from(taskEntity);
    }

    @Test
    public void testGetById_Found() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
        when(mapper.from(taskEntity)).thenReturn(task);

        Optional<Task> foundTask = taskRepositoryAdapter.get(taskId);

        assertTrue(foundTask.isPresent());
        assertEquals(task.getId(), foundTask.get().getId());
        verify(taskRepository, times(1)).findById(taskId);
        verify(mapper, times(1)).from(taskEntity);
    }

    @Test
    public void testGetById_NotFound() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        Optional<Task> foundTask = taskRepositoryAdapter.get(taskId);

        assertFalse(foundTask.isPresent());
        verify(taskRepository, times(1)).findById(taskId);
        verify(mapper, never()).from(any(TaskEntity.class));
    }

    @Test
    public void testFilterTasks() {
        Page<TaskEntity> taskEntitiesPage = new PageImpl<>(Collections.singletonList(taskEntity));
        Page<Task> tasksPage = new PageImpl<>(Collections.singletonList(task));
        Pageable pageable = Pageable.unpaged();

        when(taskRepository.filterTasks(any(), any(), any(), any(), eq(pageable))).thenReturn(taskEntitiesPage);
        when(mapper.from(taskEntity)).thenReturn(task);

        Page<Task> result = taskRepositoryAdapter.filterTasks(UUID.randomUUID(), TaskPriority.HIGH, TaskStatus.IN_PROGRESS, UUID.randomUUID(), pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(task.getId(), result.getContent().get(0).getId());
        verify(taskRepository, times(1)).filterTasks(any(), any(), any(), any(), eq(pageable));
        verify(mapper, times(1)).from(taskEntity);
    }
}
