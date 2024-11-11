package task.manager.challenge.core.business;

import task.manager.challenge.core.command.Command;
import task.manager.challenge.domain.model.Task;

import java.util.Optional;

public interface CreateTaskPort extends Command<Optional<Task>> {
}
