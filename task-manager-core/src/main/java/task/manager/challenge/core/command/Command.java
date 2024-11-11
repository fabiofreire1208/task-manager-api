package task.manager.challenge.core.command;

public interface Command<R> {
    R process(final Context context);
}
