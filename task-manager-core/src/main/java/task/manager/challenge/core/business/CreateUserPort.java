package task.manager.challenge.core.business;

import task.manager.challenge.core.command.Command;
import task.manager.challenge.domain.model.User;

import java.util.Optional;

public interface CreateUserPort extends Command<Optional<User>> {
}
