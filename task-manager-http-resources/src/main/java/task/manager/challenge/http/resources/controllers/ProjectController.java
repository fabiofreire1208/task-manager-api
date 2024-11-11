package task.manager.challenge.http.resources.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task.manager.challenge.core.business.CreateProjectPort;
import task.manager.challenge.core.command.Context;
import task.manager.challenge.domain.model.Project;
import task.manager.challenge.http.resources.dto.ProjectDto;
import task.manager.challenge.http.resources.mappers.ProjectModelMapper;

@RestController
@RequestMapping(value = "/projects")
public class ProjectController {
    private final CreateProjectPort createProjectPort;
    private final ProjectModelMapper projectModelMapper;

    @Autowired
    public ProjectController(CreateProjectPort createProjectPort, ProjectModelMapper projectModelMapper) {
        this.createProjectPort = createProjectPort;
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
}
