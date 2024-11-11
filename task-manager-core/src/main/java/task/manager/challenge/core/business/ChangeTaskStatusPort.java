package task.manager.challenge.core.business;

import task.manager.challenge.core.command.Command;
import task.manager.challenge.core.command.Context;

public interface ChangeTaskStatusPort extends Command<Void> {

    @Override
    Void process(Context context);
}
