package task.manager.challenge.adapters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.exception.UserNotFoundException;
import task.manager.challenge.core.persistence.DeviceRepositoryPort;
import task.manager.challenge.core.persistence.UserRepositoryPort;
import task.manager.challenge.domain.model.Device;
import task.manager.challenge.domain.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateDeviceAdapterTest {

    @Mock
    private DeviceRepositoryPort deviceRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private CreateDeviceAdapter createDeviceAdapter;

    @Test
    public void testProcess_SuccessfulDeviceCreation() {
        String email = "user@example.com";
        Device device = new Device();
        device.setDeviceName("My device");

        User user = new User();
        user.setName("User Test");
        user.setEmail(email);

        device.setUser(user);
        Context context = new Context();
        context.setData(device);

        when(userRepositoryPort.getByEmail(email)).thenReturn(Optional.of(user));
        when(deviceRepositoryPort.save(device)).thenReturn(device);

        Optional<Device> result = createDeviceAdapter.process(context);

        assertTrue(result.isPresent());
        assertEquals(user, device.getUser());

        verify(userRepositoryPort, times(1)).getByEmail(email);
        verify(deviceRepositoryPort, times(1)).save(device);
    }

    @Test
    public void testProcess_UserNotFound() {
        String email = "user@example.com";
        Device device = new Device();
        device.setDeviceName("My device");

        User user = new User();
        user.setName("User Test");
        user.setEmail(email);

        device.setUser(user);

        Context context = new Context();
        context.setData(device);

        when(userRepositoryPort.getByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> createDeviceAdapter.process(context));

        verify(userRepositoryPort, times(1)).getByEmail(email);
        verify(deviceRepositoryPort, never()).save(any());
    }
}
