package task.manager.challenge.core.business;

import task.manager.challenge.core.command.Command;
import task.manager.challenge.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface ListUserPort extends Command<List<Optional<User>>> {
}
