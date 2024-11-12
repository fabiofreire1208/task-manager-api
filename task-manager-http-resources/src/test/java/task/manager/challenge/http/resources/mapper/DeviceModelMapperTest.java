package task.manager.challenge.http.resources.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import task.manager.challenge.domain.model.Device;
import task.manager.challenge.http.resources.dto.DeviceDto;
import task.manager.challenge.http.resources.dto.UserDto;
import task.manager.challenge.http.resources.mappers.DeviceModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DeviceModelMapperTest {

    private final DeviceModelMapper deviceModelMapper = Mappers.getMapper(DeviceModelMapper.class);

    @Test
    public void testFromDeviceDtoToDevice() {
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setDeviceName("Test Device");

        UserDto userDto = new UserDto();
        userDto.setEmail("user.test@example.com");

        deviceDto.setUser(userDto);

        Device device = deviceModelMapper.from(deviceDto);

        assertNotNull(device);
        assertEquals(deviceDto.getDeviceName(), device.getDeviceName());
        assertEquals(deviceDto.getUser().getEmail(), device.getUser().getEmail());
    }
}
