package task.manager.challenge.http.resources.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import task.manager.challenge.domain.model.User;
import task.manager.challenge.http.resources.dto.UserDto;
import task.manager.challenge.http.resources.mappers.UserModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserModelMapperTest {

    private final UserModelMapper userModelMapper = Mappers.getMapper(UserModelMapper.class);

    @Test
    public void testFromUserDtoToUser() {
        UserDto userDto = new UserDto();
        userDto.setName("User Test");
        userDto.setEmail("user.test@example.com");

        User user = userModelMapper.from(userDto);

        assertNotNull(user);
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }
}
