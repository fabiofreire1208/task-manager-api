package task.manager.challenge.core.business;

import task.manager.challenge.core.command.Command;
import task.manager.challenge.domain.model.Device;

import java.util.List;
import java.util.Optional;

public interface ListDevicePort extends Command<List<Optional<Device>>> {
}
