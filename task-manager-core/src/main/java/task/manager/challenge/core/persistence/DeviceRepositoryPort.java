package task.manager.challenge.core.persistence;

import task.manager.challenge.domain.model.Device;

import java.util.Optional;
import java.util.UUID;

public interface DeviceRepositoryPort {

    Device save(final Device obj);
    Optional<Device> get(final UUID id);
}
