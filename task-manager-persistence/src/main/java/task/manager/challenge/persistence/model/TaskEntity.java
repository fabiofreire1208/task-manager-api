package task.manager.challenge.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import task.manager.challenge.domain.enums.TaskPriority;
import task.manager.challenge.domain.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    private UUID id;
    
    private String title;
    private String description;
    
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.OPENED;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority = TaskPriority.NORMAL;

    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private ProjectEntity project;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity assignedUser;

    @ManyToMany
    @JoinTable(
            name = "task_watchers",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> watchers;
}
