package org.dummy;

import org.testcontainers.containers.ContainerLaunchException;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Test utils.
 */
public final class TestUtils {

    static final Logger LOG = Logger.getLogger(TestUtils.class.getSimpleName());

    /**
     * Constructors.
     */
    private TestUtils() {
        // Utils
    }

    static final String POSTGRES_HOST = "postgres";
    static final int POSTGRES_PORT = 5432;

    static final String PGADMIN_HOST = "pgadmin";
    static final int PGADMIN_PORT = 5050;

    static final String ADMINER_HOST = "adminer";
    static final int ADMINER_PORT = 8080;

    static final String PGADMIN_PING = "/misc/ping";

    static final String HTTP_COLON_DOUBLE_SLASH = "http://";

    static final String PING = "PING";

    static final String EMPTY_STRING = "";

    /**
     * Perform HTTP GET via {@link HttpClient}.
     *
     * @param url URL
     * @return {@link String} of {@link HttpResponse#body()}
     */
    static String doGet(String url) {
        String body = EMPTY_STRING;
        try {
            final HttpClient httpClient = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, response.statusCode());
            body = response.body();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "URL " + url + " is unreachable", e);
        }
        return body;
    }

    /**
     * Build {@link DockerComposeContainer} by {@link List} of compose files.
     *
     * @param pathNames {@link List} of compose files
     * @return {@link DockerComposeContainer}
     * One {@link DockerComposeContainer} creates a common network for all compose file containers
     */
    static DockerComposeContainer build(List<String> pathNames) {
        return new DockerComposeContainer(pathNames.stream().map(File::new).collect(Collectors.toList()))
                .withLocalCompose(true)
                .withOptions("--compatibility")
                .withExposedService(POSTGRES_HOST, POSTGRES_PORT, Wait.forHealthcheck())
                .withExposedService(PGADMIN_HOST, PGADMIN_PORT,
                        Wait.forHttp(PGADMIN_PING)
                                .forStatusCode(HttpURLConnection.HTTP_OK)
                                .withStartupTimeout(Duration.ofSeconds(120L)))
                .withExposedService(ADMINER_HOST, ADMINER_PORT,
                        Wait.forHttp("/").forStatusCode(HttpURLConnection.HTTP_OK));
    }

    /**
     * Stop {@link DockerComposeContainer} without {@link ContainerLaunchException}.
     *
     * @param containers {@link DockerComposeContainer}
     */
    static void stopDockerCompose(DockerComposeContainer... containers) {
        for (DockerComposeContainer container : containers) {
            try {
                if (container != null) {
                    container.stop();
                }
            } catch (ContainerLaunchException e) {
                // can not remove network
            }
        }
    }
}
