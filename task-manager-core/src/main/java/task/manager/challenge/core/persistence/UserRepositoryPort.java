package task.manager.challenge.core.persistence;

import task.manager.challenge.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {

    User save(final User obj);
    Optional<User> get(final UUID id);
    Optional<User> getByEmail(final String email);
    List<Optional<User>> findAllUsers();
}
