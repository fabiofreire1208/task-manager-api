package task.manager.challenge.adapters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.manager.challenge.core.business.ListUserPort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.core.persistence.UserRepositoryPort;
import task.manager.challenge.domain.model.User;
import task.manager.challenge.persistence.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ListUserAdapter implements ListUserPort {
    private final UserRepositoryPort userRepositoryPort;

    @Autowired
    public ListUserAdapter(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public List<Optional<User>> process(Context context) {
        return userRepositoryPort.findAllUsers();
    }
}
