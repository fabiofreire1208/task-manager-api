package task.manager.challenge.http.resources.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task.manager.challenge.core.business.CreateUserPort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.domain.model.User;
import task.manager.challenge.http.resources.dto.UserDto;
import task.manager.challenge.http.resources.mappers.UserModelMapper;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final CreateUserPort createUserPort;
    private final UserModelMapper userModelMapper;

    @Autowired
    public UserController(CreateUserPort createUserPort, UserModelMapper userModelMapper) {
        this.createUserPort = createUserPort;
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
}
