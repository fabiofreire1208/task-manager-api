package task.manager.challenge.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "task.manager.challenge")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
