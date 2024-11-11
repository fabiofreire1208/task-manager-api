package task.manager.challenge.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.manager.challenge.core.business.CreateDevicePort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.exception.UserNotFoundException;
import task.manager.challenge.core.persistence.DeviceRepositoryPort;
import task.manager.challenge.core.persistence.UserRepositoryPort;
import task.manager.challenge.domain.model.Device;
import task.manager.challenge.domain.model.User;

import java.util.Optional;

@Service
public class CreateDeviceAdapter implements CreateDevicePort {
    private final DeviceRepositoryPort deviceRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Autowired
    public CreateDeviceAdapter(DeviceRepositoryPort deviceRepositoryPort, UserRepositoryPort userRepositoryPort) {
        this.deviceRepositoryPort = deviceRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Optional<Device> process(Context context) {
        final Device data = context.getData(Device.class);
        final User user = userRepositoryPort.getByEmail(data.getUser().getEmail()).orElseThrow(
                () -> new UserNotFoundException("User not found!")
        );
        data.setUser(user);
        return Optional.ofNullable(deviceRepositoryPort.save(data));
    }
}
