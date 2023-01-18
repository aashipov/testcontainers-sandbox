package org.dummy;

import java.io.File;
import java.net.HttpURLConnection;
import java.time.Duration;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.ContainerLaunchException;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import static org.dummy.TestConstants.*;

public class MultipleComposeFileTest {

    static DockerComposeContainer postgresContainer = null;
    static DockerComposeContainer pgadminContainer = null;

    /**
     * ${@link BeforeClass} with {@link AfterClass} as more natural alternative to {@link ClassRule}.
     */
    @BeforeClass
    public static void setUp() {
        postgresContainer =
                new DockerComposeContainer(new File("src/test/resources/b/docker-compose-postgres.yml"))
                        .withLocalCompose(true)
                        // --compatibility flag to run testcontainers against Docker Compose V 2 LOCAL
                        .withOptions("--compatibility")
                        .withExposedService("postgres", POSTGRES_PORT, Wait.forHealthcheck()
                        );
        pgadminContainer =
                new DockerComposeContainer(new File("src/test/resources/b/docker-compose-pgadmin.yml"))
                        .withLocalCompose(true)
                        // --compatibility flag to run testcontainers against Docker Compose V 2 LOCAL
                        .withOptions("--compatibility")
                        .withExposedService("pgadmin", PGADMIN_PORT,
                                Wait.forHttp(PGADMIN_PING)
                                        .forStatusCode(HttpURLConnection.HTTP_OK)
                                        .withStartupTimeout(Duration.ofSeconds(120L))
                        );
        postgresContainer.start();
        pgadminContainer.start();
    }

    @AfterClass
    public static void tearDown() {
        try {
            if (pgadminContainer != null) {
                pgadminContainer.stop();
            }
        } catch (ContainerLaunchException e) {
            // can not remove network as postgres container still use it
        }
        if (postgresContainer != null) {
            postgresContainer.stop();
        }
    }

    @Test
    public void test() {
        checkPgadminPingEndpoint();
    }
}
