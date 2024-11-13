package task.manager.challenge.http.resources.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task.manager.challenge.core.business.CreateDevicePort;
import task.manager.challenge.core.business.ListDevicePort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.domain.model.Device;
import task.manager.challenge.domain.model.Project;
import task.manager.challenge.http.resources.dto.DeviceDto;
import task.manager.challenge.http.resources.mappers.DeviceModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/devices")
public class DeviceController {
    private final CreateDevicePort createDevicePort;
    private final ListDevicePort listDevicePort;
    private final DeviceModelMapper deviceModelMapper;

    @Autowired
    public DeviceController(CreateDevicePort createDevicePort, ListDevicePort listDevicePort, DeviceModelMapper deviceModelMapper) {
        this.createDevicePort = createDevicePort;
        this.listDevicePort = listDevicePort;
        this.deviceModelMapper = deviceModelMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDevice(@RequestBody DeviceDto deviceDto) {
        final Device device = deviceModelMapper.from(deviceDto);

        final Context context = new Context();
        context.setData(device);

        createDevicePort.process(context);

        return ResponseEntity.ok("The device was added to the user successfully!");
    }

    @GetMapping("/list")
    public ResponseEntity<List<Device>> getAllDevices() {
        final Context context = new Context();
        List<Optional<Device>> optionalDevices = listDevicePort.process(context);

        List<Device> devices = optionalDevices.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return ResponseEntity.ok(devices);
    }
}
