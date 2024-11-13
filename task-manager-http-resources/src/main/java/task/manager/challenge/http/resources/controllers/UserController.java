package task.manager.challenge.http.resources.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task.manager.challenge.core.business.CreateUserPort;
import task.manager.challenge.core.business.ListUserPort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.domain.model.User;
import task.manager.challenge.http.resources.dto.UserDto;
import task.manager.challenge.http.resources.mappers.UserModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final CreateUserPort createUserPort;
    private final ListUserPort listUserPort;
    private final UserModelMapper userModelMapper;

    @Autowired
    public UserController(CreateUserPort createUserPort, ListUserPort listUserPort, UserModelMapper userModelMapper) {
        this.createUserPort = createUserPort;
        this.listUserPort = listUserPort;
        this.userModelMapper = userModelMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody UserDto userRequest) {
        final User user = userModelMapper.from(userRequest);

        final Context context = new Context();
        context.setData(user);

        createUserPort.process(context);

        return ResponseEntity.ok("The user was created successfully!");
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        final Context context = new Context();
        List<Optional<User>> optionalUsers = listUserPort.process(context);

        List<User> users = optionalUsers.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return ResponseEntity.ok(users);
    }
}
