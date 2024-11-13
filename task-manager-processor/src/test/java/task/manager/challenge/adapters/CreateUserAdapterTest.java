package task.manager.challenge.adapters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.exception.UserAlreadyExistsException;
import task.manager.challenge.core.exception.UserNotFoundException;
import task.manager.challenge.core.persistence.UserRepositoryPort;
import task.manager.challenge.domain.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserAdapterTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private CreateUserAdapter createUserAdapter;

    @Test
    public void testProcess_SuccessfulUserCreation() {
        User user = new User();
        user.setName("User Test");
        user.setEmail("user.test@example.com");

        Context context = new Context();
        context.setData(user);

        when(userRepositoryPort.getByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepositoryPort.save(user)).thenReturn(user);

        Optional<User> result = createUserAdapter.process(context);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());

        verify(userRepositoryPort, times(1)).getByEmail(anyString());
        verify(userRepositoryPort, times(1)).save(user);
    }

    @Test
    public void testProcess_ErrorUsersAlreadyExists() {
        User user = new User();
        user.setName("User Test");
        user.setEmail("user.test@example.com");

        Context context = new Context();
        context.setData(user);

        when(userRepositoryPort.getByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () ->createUserAdapter.process(context));

        verify(userRepositoryPort, times(1)).getByEmail(anyString());
        verify(userRepositoryPort, times(0)).save(user);
    }
}
