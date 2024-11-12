package task.manager.challenge.persistence.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.manager.challenge.domain.model.User;
import task.manager.challenge.persistence.mappers.PersistenceEntityMapper;
import task.manager.challenge.persistence.model.UserEntity;
import task.manager.challenge.persistence.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryAdapterTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersistenceEntityMapper mapper;

    @InjectMocks
    private UserRepositoryAdapter userRepositoryAdapter;

    private User user;
    private UserEntity userEntity;
    private UUID userId;

    @BeforeEach
    public void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");

        userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail("test@example.com");
    }

    @Test
    public void testSave() {
        when(mapper.from(user)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(mapper.from(userEntity)).thenReturn(user);

        User savedUser = userRepositoryAdapter.save(user);

        assertNotNull(savedUser);
        assertEquals(user.getId(), savedUser.getId());
        verify(userRepository, times(1)).save(userEntity);
        verify(mapper, times(1)).from(user);
        verify(mapper, times(1)).from(userEntity);
    }

    @Test
    public void testGetById_Found() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(mapper.from(userEntity)).thenReturn(user);

        Optional<User> foundUser = userRepositoryAdapter.get(userId);

        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
        verify(userRepository, times(1)).findById(userId);
        verify(mapper, times(1)).from(userEntity);
    }

    @Test
    public void testGetById_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepositoryAdapter.get(userId);

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(userId);
        verify(mapper, never()).from(any(UserEntity.class));
    }

    @Test
    public void testGetByEmail_Found() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(userEntity));
        when(mapper.from(userEntity)).thenReturn(user);

        Optional<User> foundUser = userRepositoryAdapter.getByEmail(user.getEmail());

        assertTrue(foundUser.isPresent());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(mapper, times(1)).from(userEntity);
    }

    @Test
    public void testGetByEmail_NotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepositoryAdapter.getByEmail(user.getEmail());

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(mapper, never()).from(any(UserEntity.class));
    }
}
