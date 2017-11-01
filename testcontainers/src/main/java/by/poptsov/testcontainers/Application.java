package by.poptsov.testcontainers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODO: description
 *
 * @author Alexey Poptsov
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

}
