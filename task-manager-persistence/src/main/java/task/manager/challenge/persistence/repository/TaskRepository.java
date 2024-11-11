package task.manager.challenge.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import task.manager.challenge.domain.enums.TaskPriority;
import task.manager.challenge.domain.enums.TaskStatus;
import task.manager.challenge.persistence.model.TaskEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    @Query("SELECT t FROM TaskEntity t " +
            "LEFT JOIN t.watchers w " +
            "WHERE (:userId IS NULL OR t.assignedUser.id = :userId) " +
            "AND (:priority IS NULL OR t.priority = :priority) " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:projectId IS NULL OR t.project.id = :projectId)")
    Page<TaskEntity> filterTasks(@Param("userId") UUID userId,
                                 @Param("priority") TaskPriority priority,
                                 @Param("status") TaskStatus status,
                                 @Param("projectId") UUID projectId,
                                 Pageable pageable);

    @Query("SELECT t FROM TaskEntity t " +
            "LEFT JOIN FETCH t.watchers w " +
            "LEFT JOIN FETCH w.devices " +
            "WHERE t.id = :taskId")
    Optional<TaskEntity> findByIdWithWatchersAndDevices(@Param("taskId") UUID taskId);
}
