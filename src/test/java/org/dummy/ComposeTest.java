package org.dummy;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.DockerComposeContainer;

import static org.dummy.TestUtils.*;
import static org.junit.Assert.*;

/**
 * Common ancestor.
 */
public abstract class ComposeTest {

    protected static DockerComposeContainer dockerComposeContainer = null;

    /**
     * ${@link BeforeClass} with {@link AfterClass} as more natural alternative to {@link ClassRule}.
     */
    @AfterClass
    public static void tearDown() {
        stopDockerCompose(dockerComposeContainer);
    }

    @Test
    public void test() {
        assertNotNull(dockerComposeContainer);
        String pgadminPing = doGet(HTTP_COLON_DOUBLE_SLASH
                + dockerComposeContainer.getServiceHost(PGADMIN_HOST, PGADMIN_PORT)
                + ":"
                + dockerComposeContainer.getServicePort(PGADMIN_HOST, PGADMIN_PORT)
                + PGADMIN_PING);
        assertEquals(PING, pgadminPing);
        String adminer = doGet(HTTP_COLON_DOUBLE_SLASH
                + dockerComposeContainer.getServiceHost(ADMINER_HOST, ADMINER_PORT)
                + ":"
                + dockerComposeContainer.getServicePort(ADMINER_HOST, ADMINER_PORT));
        assertTrue(adminer.contains("<tr><th>Server<td><input name=\"auth[server]\" value=\"postgres\" title=\"hostname[:port]\" placeholder=\"localhost\" autocapitalize=\"off\">"));
    }
}
