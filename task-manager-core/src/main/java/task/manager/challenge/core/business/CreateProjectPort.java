package task.manager.challenge.core.business;

import task.manager.challenge.core.command.Command;
import task.manager.challenge.domain.model.Project;

import java.util.Optional;

public interface CreateProjectPort extends Command<Optional<Project>> {
}
