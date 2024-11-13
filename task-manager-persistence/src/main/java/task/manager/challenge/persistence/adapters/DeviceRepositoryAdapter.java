package task.manager.challenge.persistence.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import task.manager.challenge.core.persistence.DeviceRepositoryPort;
import task.manager.challenge.domain.model.Device;
import task.manager.challenge.persistence.mappers.PersistenceEntityMapper;
import task.manager.challenge.persistence.repository.DeviceRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DeviceRepositoryAdapter implements DeviceRepositoryPort {
    private final DeviceRepository deviceRepository;
    private final PersistenceEntityMapper mapper;

    @Autowired
    public DeviceRepositoryAdapter(DeviceRepository deviceRepository, PersistenceEntityMapper mapper) {
        this.deviceRepository = deviceRepository;
        this.mapper = mapper;
    }

    @Override
    public Device save(Device obj) {
        return mapper.from(deviceRepository.save(mapper.from(obj)));
    }

    @Override
    public Optional<Device> get(UUID id) {
        Device device = deviceRepository.findById(id)
                .map(mapper::from)
                .orElse(null);

        return Objects.nonNull(device) ? Optional.of(device) : Optional.empty();
    }

    @Override
    public List<Optional<Device>> findAllDevices() {
        return deviceRepository.findAll().stream()
                .map(mapper::from)
                .map(Optional::of)
                .toList();
    }
}
