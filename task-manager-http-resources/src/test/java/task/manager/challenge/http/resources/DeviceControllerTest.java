package task.manager.challenge.http.resources;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import task.manager.challenge.core.business.CreateDevicePort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.exception.UserNotFoundException;
import task.manager.challenge.domain.model.Device;
import task.manager.challenge.domain.model.User;
import task.manager.challenge.http.resources.controllers.DeviceController;
import task.manager.challenge.http.resources.dto.DeviceDto;
import task.manager.challenge.http.resources.dto.UserDto;
import task.manager.challenge.http.resources.mappers.DeviceModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceControllerTest {

    @Mock
    private CreateDevicePort createDevicePort;

    @Mock
    private DeviceModelMapper deviceModelMapper;

    @InjectMocks
    private DeviceController deviceController;

    @Test
    public void addDevice() throws Exception {
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setDeviceName("My device");

        UserDto userDto = new UserDto();
        userDto.setName("User Test");
        userDto.setEmail("user.test@example.com");

        deviceDto.setUser(userDto);

        Device device = new Device();
        device.setDeviceName("My device");

        User user = new User();
        user.setName("User Test");
        user.setEmail("user.test@example.com");

        device.setUser(user);

        when(deviceModelMapper.from(deviceDto)).thenReturn(device);
        when(createDevicePort.process(any(Context.class))).thenReturn(Optional.of(device));

        ResponseEntity<String> response = deviceController.addDevice(deviceDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("The device was added to the user successfully!", response.getBody());

        verify(deviceModelMapper, times(1)).from(deviceDto);
        verify(createDevicePort, times(1)).process(any(Context.class));
    }

    @Test
    public void addDeviceWithUserNotFound() throws Exception {
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setDeviceName("My device");

        UserDto userDto = new UserDto();
        userDto.setName("User Test");
        userDto.setEmail("user.test@example.com");

        deviceDto.setUser(userDto);

        Device device = new Device();
        device.setDeviceName("My device");

        User user = new User();
        user.setName("User Test");
        user.setEmail("user.test@example.com");

        device.setUser(user);

        when(deviceModelMapper.from(deviceDto)).thenReturn(device);
        doThrow(new UserNotFoundException("User not found!")).when(createDevicePort).process(any(Context.class));

        RuntimeException exception = assertThrows(UserNotFoundException.class, () -> {
            deviceController.addDevice(deviceDto);
        });

        assertEquals("User not found!", exception.getMessage());

        verify(deviceModelMapper, times(1)).from(deviceDto);
        verify(createDevicePort, times(1)).process(any(Context.class));
    }
}
