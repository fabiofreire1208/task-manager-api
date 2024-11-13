package task.manager.challenge.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.manager.challenge.core.business.ListDevicePort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.persistence.DeviceRepositoryPort;
import task.manager.challenge.domain.model.Device;

import java.util.List;
import java.util.Optional;

@Service
public class ListDeviceAdapter implements ListDevicePort {
    private final DeviceRepositoryPort deviceRepositoryPort;

    @Autowired
    public ListDeviceAdapter(DeviceRepositoryPort deviceRepositoryPort) {
        this.deviceRepositoryPort = deviceRepositoryPort;
    }

    @Override
    public List<Optional<Device>> process(Context context) {
        return deviceRepositoryPort.findAllDevices();
    }
}
