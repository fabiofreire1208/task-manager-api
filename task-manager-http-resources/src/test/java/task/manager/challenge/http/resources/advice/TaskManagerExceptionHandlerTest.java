package task.manager.challenge.http.resources.advice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import task.manager.challenge.core.exception.ProjectNotFoundException;
import task.manager.challenge.core.exception.TaskNotFoundException;
import task.manager.challenge.core.exception.UserNotFoundException;
import task.manager.challenge.http.resources.dto.ErrorResponse;
import task.manager.challenge.http.resources.exception.TaskManagerExceptionHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TaskManagerExceptionHandlerTest {

    @Mock
    private WebRequest webRequest;

    private final TaskManagerExceptionHandler exceptionHandler = new TaskManagerExceptionHandler();

    @Test
    public void testHandleGeneralException() {
        Exception ex = new Exception("Something went wrong");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGeneralException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Something went wrong", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    public void testHandleTaskNotFoundException() {
        TaskNotFoundException ex = new TaskNotFoundException("Task not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Task not found", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    public void testHandleUserNotFoundException() {
        UserNotFoundException ex = new UserNotFoundException("User not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    public void testHandleProjectNotFoundException() {
        ProjectNotFoundException ex = new ProjectNotFoundException("Project not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Project not found", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }
}
