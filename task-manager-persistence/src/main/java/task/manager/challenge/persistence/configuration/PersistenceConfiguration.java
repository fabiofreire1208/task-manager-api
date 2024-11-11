package task.manager.challenge.persistence.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {"task.manager.challenge.persistence"})
@EntityScan(basePackages = {"task.manager.challenge.persistence.model"})
@EnableJpaRepositories(basePackages = {"task.manager.challenge.persistence"})
@EnableTransactionManagement
public class PersistenceConfiguration {
}
