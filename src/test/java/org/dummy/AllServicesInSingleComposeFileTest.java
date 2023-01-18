package org.dummy;

import java.io.File;
import java.net.HttpURLConnection;
import java.time.Duration;

import org.junit.*;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import static org.dummy.TestConstants.*;

/**
 * Single docker-compose file with inheritance, environment showcase.
 */
public class AllServicesInSingleComposeFileTest {

    static DockerComposeContainer dockerComposeContainer = null;

    /**
     * ${@link BeforeClass} with {@link AfterClass} as more natural alternative to {@link ClassRule}.
     */
    @BeforeClass
    public static void setUp() {
        dockerComposeContainer =
                new DockerComposeContainer(new File("src/test/resources/a/docker-compose.yml"))
                        .withLocalCompose(true)
                        // --compatibility flag to run testcontainers against Docker Compose V 2 LOCAL
                        .withOptions("--compatibility")
                        .withExposedService("postgres", POSTGRES_PORT, Wait.forHealthcheck())
                        .withExposedService("pgadmin", PGADMIN_PORT,
                                Wait.forHttp(PGADMIN_PING)
                                        .forStatusCode(HttpURLConnection.HTTP_OK)
                                        .withStartupTimeout(Duration.ofSeconds(120L))
                        )
        ;
        dockerComposeContainer.start();
    }

    @AfterClass
    public static void tearDown() {
        if (dockerComposeContainer != null) {
            dockerComposeContainer.stop();
        }
    }

    @Test
    public void test() {
        checkPgadminPingEndpoint();
    }
}
