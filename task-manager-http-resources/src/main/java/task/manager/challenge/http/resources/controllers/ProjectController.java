package task.manager.challenge.http.resources.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task.manager.challenge.core.business.CreateProjectPort;
import task.manager.challenge.core.business.ListProjectPort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.domain.model.Project;
import task.manager.challenge.domain.model.User;
import task.manager.challenge.http.resources.dto.ProjectDto;
import task.manager.challenge.http.resources.mappers.ProjectModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/projects")
public class ProjectController {
    private final CreateProjectPort createProjectPort;
    private final ListProjectPort listProjectPort;
    private final ProjectModelMapper projectModelMapper;

    @Autowired
    public ProjectController(CreateProjectPort createProjectPort, ListProjectPort listProjectPort, ProjectModelMapper projectModelMapper) {
        this.createProjectPort = createProjectPort;
        this.listProjectPort = listProjectPort;
        this.projectModelMapper = projectModelMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProject(@RequestBody ProjectDto projectRequest) {
        final Project project = projectModelMapper.from(projectRequest);

        final Context context = new Context();
        context.setData(project);

        createProjectPort.process(context);

        return ResponseEntity.ok("The project was created successfully!");
    }

    @GetMapping("/list")
    public ResponseEntity<List<Project>> getAllProjects() {
        final Context context = new Context();
        List<Optional<Project>> optionalProjects = listProjectPort.process(context);

        List<Project> projects = optionalProjects.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return ResponseEntity.ok(projects);
    }
}
