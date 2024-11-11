package task.manager.challenge.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {
    private UUID id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime deadline;
    private User assignedUser;
    private Project project;
    private List<User> watchers;
}
