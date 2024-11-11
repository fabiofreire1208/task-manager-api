package task.manager.challenge.core.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import task.manager.challenge.domain.enums.TaskPriority;
import task.manager.challenge.domain.enums.TaskStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import static java.util.Optional.ofNullable;

public class Context extends HashMap<String, Object> implements Serializable {
    @Getter
    @AllArgsConstructor
    private enum Parameters {
        DATA("data"),
        RESUlT("result"),
        USER_ID("userId"),
        PRIORITY("priority"),
        STATUS("status"),
        PROJECT_ID("projectId"),
        PAGEABLE("pageable");

        private final String value;
    }

    public Context(final Object data) {
        super.put(Parameters.DATA.value, data);
    }

    public Context() {
        super();
    }
    public Class<?> getDataClass() {
        return ofNullable(get(Parameters.DATA.value))
                .map(Object::getClass)
                .orElse(null);
    }

    public Class<?> getResultClass() {
        return ofNullable(get(Parameters.RESUlT.value))
                .map(Object::getClass)
                .orElse(null);
    }

    public <T> T getData(final Class<T> clazz) {
        return getProperty(Parameters.DATA.value, clazz);
    }

    public void setData(final Object data) {
        put(Parameters.DATA.value, data);
    }

    public <T> T getResult(final Class<T> clazz) {
        return getProperty(Parameters.RESUlT.value, clazz);
    }

    public void setResult(final Object result) {
        put(Parameters.RESUlT.value, result);
    }

    public void setUserId(UUID userId) {
        put(Parameters.USER_ID.value, userId);
    }

    public UUID getUserId() {
        return getProperty(Parameters.USER_ID.value, UUID.class);
    }

    public TaskPriority getPriority() {
        return getProperty(Parameters.PRIORITY.value, TaskPriority.class);
    }

    public void setPriority(TaskPriority priority) {
        put(Parameters.PRIORITY.value, priority);
    }

    public void setStatus(TaskStatus status) {
        put(Parameters.STATUS.value, status);
    }

    public TaskStatus getStatus() {
        return getProperty(Parameters.STATUS.value, TaskStatus.class);
    }

    public void setProjectId(UUID projectId) {
        put(Parameters.PROJECT_ID.value, projectId);
    }

    public UUID getProjectId() {
        return getProperty(Parameters.PROJECT_ID.value, UUID.class);
    }

    public void setPageable(Pageable pageable) {
        put(Parameters.PAGEABLE.value, pageable);
    }

    public Pageable getPageable() {
        return getProperty(Parameters.PAGEABLE.value, Pageable.class);
    }

    public <R> R getProperty(final String key, final Class<R> clazz) {
        return ofNullable(get(key))
                .map(clazz::cast)
                .orElse(null);
    }
}
