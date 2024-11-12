package task.manager.challenge.http.resources;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import task.manager.challenge.core.business.CreateUserPort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.domain.model.User;
import task.manager.challenge.http.resources.controllers.UserController;
import task.manager.challenge.http.resources.dto.UserDto;
import task.manager.challenge.http.resources.mappers.UserModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private CreateUserPort createUserPort;

    @Mock
    private UserModelMapper userModelMapper;

    @InjectMocks
    private UserController userController;

    @Test
    public void testAddUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("User Test");
        userDto.setEmail("user.test@example.com");

        User user = new User();
        user.setName("User Test");
        user.setEmail("user.test@example.com");

        when(userModelMapper.from(userDto)).thenReturn(user);
        when(createUserPort.process(any(Context.class))).thenReturn(Optional.of(user));

        ResponseEntity<String> response = userController.addUser(userDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("The user was created successfully!", response.getBody());

        verify(userModelMapper, times(1)).from(userDto);
        verify(createUserPort, times(1)).process(any(Context.class));
    }
}
