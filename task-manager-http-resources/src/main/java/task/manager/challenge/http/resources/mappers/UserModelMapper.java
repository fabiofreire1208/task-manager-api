package task.manager.challenge.http.resources.mappers;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import task.manager.challenge.domain.model.User;
import task.manager.challenge.http.resources.dto.UserDto;

@Mapper(builder = @Builder(disableBuilder = true), componentModel = "spring")
public interface UserModelMapper {

    User from(final UserDto source);
}
