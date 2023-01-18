package org.dummy;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * Test constants.
 */
public final class TestConstants {

    static final Logger LOG = Logger.getLogger(TestConstants.class.getSimpleName());

    /**
     * Constructors.
     */
    private TestConstants() {
        // Constants
    }

    static final int POSTGRES_PORT = 5432;
    static final int PGADMIN_PORT = 5050;

    static final String PGADMIN_PING = "/misc/ping";

    static final String PGADMIN_PING_URL = "http://0.0.0.0" + ":" + PGADMIN_PORT + PGADMIN_PING;

    static final String PING = "PING";

    static void checkPgadminPingEndpoint() {
        try {
            final HttpClient httpClient = HttpClient.newBuilder().build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(PGADMIN_PING_URL))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, response.statusCode());
            assertEquals(PING, PING, response.body());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "pgadmin PING endpoint unreachable", e);
        }
    }
}
