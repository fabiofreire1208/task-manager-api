package task.manager.challenge.http.resources.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import task.manager.challenge.domain.enums.TaskStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskStatusDto {
    private TaskStatus status;
}
