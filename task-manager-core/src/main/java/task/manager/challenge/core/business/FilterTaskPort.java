package task.manager.challenge.core.business;

import org.springframework.data.domain.Page;
import task.manager.challenge.core.command.Command;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.domain.model.Task;

import java.util.Optional;

public interface FilterTaskPort extends Command<Optional<Page<Task>>> {

    @Override
    Optional<Page<Task>> process(Context context);
}
