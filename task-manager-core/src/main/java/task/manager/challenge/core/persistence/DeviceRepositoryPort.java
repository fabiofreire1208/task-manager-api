package task.manager.challenge.core.persistence;

import task.manager.challenge.domain.model.Device;
import task.manager.challenge.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepositoryPort {
    Device save(final Device obj);
    Optional<Device> get(final UUID id);
    List<Optional<Device>> findAllDevices();
}
