package by.poptsov.testcontainers;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * TODO: description
 *
 * @author Alexey Poptsov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public abstract class AbstractIntegrationTest {

    private static final String REDIS_IMAGE_NAME = "redis:latest";
    private static final Integer REDIS_PORT = 6379;

    private static final String PSQL_USER = "admin";
    private static final String PSQL_PASSWORD = "admin";
    private static final String PSQL_DATABASE = "test";


    private static final String KAFKA_IMAGE_NAME = "spotify/kafka:latest";
    private static final Integer KAFKA_PORT = 9092;
    private static final Integer ZOOKEEPER_PORT = 2181;


    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            /* Don't forget to define this variables in ENV properties if you want to use private registry
            registry.url=
            registry.username=
            registry.password=
            registry.email=
             */
            redis(context);
            psql(context);
            kafka(context);
        }
    }

    private static void redis(ConfigurableApplicationContext context) {
        GenericContainer redis = new GenericContainer(REDIS_IMAGE_NAME).withExposedPorts(REDIS_PORT);
        redis.start();
        EnvironmentTestUtils.addEnvironment(
                context.getEnvironment(),
                String.format("spring.redis.host=%s", redis.getContainerIpAddress()),
                String.format("spring.redis.port=%s", redis.getMappedPort(REDIS_PORT))
        );
    }

    private static void psql(ConfigurableApplicationContext context) {
        PostgreSQLContainer psql = new PostgreSQLContainer();
        psql.withUsername(PSQL_USER);
        psql.withPassword(PSQL_PASSWORD);
        psql.start();
        EnvironmentTestUtils.addEnvironment(
                context.getEnvironment(),
                String.format("spring.datasource.url=jdbc:postgresql://%s:%s/%s", psql.getContainerIpAddress(), psql.getFirstMappedPort(), PSQL_DATABASE),
                String.format("spring.datasource.username=%s", PSQL_USER),
                String.format("spring.datasource.password=%s", PSQL_PASSWORD)
        );
    }

    private static void kafka(ConfigurableApplicationContext context) {
        GenericContainer kafka = new GenericContainer(KAFKA_IMAGE_NAME)
                .withEnv("ADVERTISED_PORT", String.valueOf(KAFKA_PORT))
                .withEnv("ADVERTISED_HOST", "localhost")
                .withEnv("AUTO_CREATE_TOPICS", "true")
                .withEnv("NUM_PARTITIONS", "1")
                .withExposedPorts(KAFKA_PORT, ZOOKEEPER_PORT);


        kafka.start();
        EnvironmentTestUtils.addEnvironment(
                context.getEnvironment(),
                String.format("kafka.servers=%s:%s", kafka.getContainerIpAddress(), kafka.getMappedPort(KAFKA_PORT))
        );

    }


}