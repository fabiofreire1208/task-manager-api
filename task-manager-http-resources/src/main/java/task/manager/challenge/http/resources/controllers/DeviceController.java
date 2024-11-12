package task.manager.challenge.http.resources.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task.manager.challenge.core.business.CreateDevicePort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.domain.model.Device;
import task.manager.challenge.http.resources.dto.DeviceDto;
import task.manager.challenge.http.resources.mappers.DeviceModelMapper;

@RestController
@RequestMapping(value = "/devices")
public class DeviceController {
    private final CreateDevicePort createDevicePort;
    private final DeviceModelMapper deviceModelMapper;

    @Autowired
    public DeviceController(CreateDevicePort createDevicePort, DeviceModelMapper deviceModelMapper) {
        this.createDevicePort = createDevicePort;
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
}
