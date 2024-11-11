package task.manager.challenge.persistence.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import task.manager.challenge.core.persistence.UserRepositoryPort;
import task.manager.challenge.domain.model.User;
import task.manager.challenge.persistence.mappers.PersistenceEntityMapper;
import task.manager.challenge.persistence.repository.UserRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final UserRepository userRepository;
    private final PersistenceEntityMapper mapper;

    @Autowired
    public UserRepositoryAdapter(UserRepository userRepository, PersistenceEntityMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User obj) {
        return mapper.from(userRepository.save(mapper.from(obj)));
    }

    @Override
    public Optional<User> get(UUID id) {
        User user = userRepository.findById(id)
                .map(mapper::from)
                .orElse(null);

        return Objects.nonNull(user) ? Optional.of(user) : Optional.empty();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .map(mapper::from)
                .orElse(null);
        return Objects.nonNull(user) ? Optional.of(user) : Optional.empty();
    }
}
