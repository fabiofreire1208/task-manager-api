package task.manager.challenge.http.resources.mappers;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import task.manager.challenge.domain.model.Device;
import task.manager.challenge.http.resources.dto.DeviceDto;

@Mapper(builder = @Builder(disableBuilder = true), componentModel = "spring")
public interface DeviceModelMapper {

    Device from(final DeviceDto source);
}
