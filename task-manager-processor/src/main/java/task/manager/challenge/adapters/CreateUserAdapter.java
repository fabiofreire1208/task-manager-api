package task.manager.challenge.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.manager.challenge.core.business.CreateUserPort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.exception.UserAlreadyExistsException;
import task.manager.challenge.core.exception.UserNotFoundException;
import task.manager.challenge.core.persistence.UserRepositoryPort;
import task.manager.challenge.domain.model.User;

import java.util.Optional;

@Service
public class CreateUserAdapter implements CreateUserPort {

    private final UserRepositoryPort userRepositoryPort;

    @Autowired
    public CreateUserAdapter(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Optional<User> process(Context context) {
        final User data = context.getData(User.class);

        if(userRepositoryPort.getByEmail(data.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User Already Exists!");
        }

        return Optional.ofNullable(userRepositoryPort.save(data));
    }
}
