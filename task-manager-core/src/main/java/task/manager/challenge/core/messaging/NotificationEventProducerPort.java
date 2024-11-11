package task.manager.challenge.core.messaging;

public interface NotificationEventProducerPort {
    void send(final String taskId);
}
