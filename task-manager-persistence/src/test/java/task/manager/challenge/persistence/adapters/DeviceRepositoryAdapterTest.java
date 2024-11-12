package task.manager.challenge.persistence.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.manager.challenge.domain.model.Device;
import task.manager.challenge.persistence.mappers.PersistenceEntityMapper;
import task.manager.challenge.persistence.model.DeviceEntity;
import task.manager.challenge.persistence.repository.DeviceRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceRepositoryAdapterTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private PersistenceEntityMapper mapper;

    @InjectMocks
    private DeviceRepositoryAdapter deviceRepositoryAdapter;

    private Device device;
    private DeviceEntity deviceEntity;
    private UUID deviceId;

    @BeforeEach
    public void setUp() {
        deviceId = UUID.randomUUID();
        device = new Device();
        device.setId(deviceId);
        device.setDeviceName("Test Device");

        deviceEntity = new DeviceEntity();
        deviceEntity.setId(deviceId);
        deviceEntity.setDeviceName("Test Device");
    }

    @Test
    public void testSave() {
        when(mapper.from(device)).thenReturn(deviceEntity);
        when(deviceRepository.save(deviceEntity)).thenReturn(deviceEntity);
        when(mapper.from(deviceEntity)).thenReturn(device);

        Device savedDevice = deviceRepositoryAdapter.save(device);

        assertNotNull(savedDevice);
        assertEquals(device.getId(), savedDevice.getId());
        verify(deviceRepository, times(1)).save(deviceEntity);
        verify(mapper, times(1)).from(device);
        verify(mapper, times(1)).from(deviceEntity);
    }

    @Test
    public void testGetById_Found() {
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(deviceEntity));
        when(mapper.from(deviceEntity)).thenReturn(device);

        Optional<Device> foundDevice = deviceRepositoryAdapter.get(deviceId);

        assertTrue(foundDevice.isPresent());
        assertEquals(device.getId(), foundDevice.get().getId());
        verify(deviceRepository, times(1)).findById(deviceId);
        verify(mapper, times(1)).from(deviceEntity);
    }

    @Test
    public void testGetById_NotFound() {
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.empty());

        Optional<Device> foundDevice = deviceRepositoryAdapter.get(deviceId);

        assertFalse(foundDevice.isPresent());
        verify(deviceRepository, times(1)).findById(deviceId);
        verify(mapper, never()).from(any(DeviceEntity.class));
    }
}
