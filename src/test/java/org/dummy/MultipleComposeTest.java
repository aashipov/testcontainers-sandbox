package org.dummy;

import org.junit.BeforeClass;

import java.util.List;

import static org.dummy.TestUtils.build;

/**
 * Multiple docker-compose file, environment showcase.
 */
public class MultipleComposeTest extends ComposeTest {

    @BeforeClass
    public static void setUp() {
        dockerComposeContainer = build(List.of(
                "src/test/resources/b/docker-compose-postgres.yml",
                "src/test/resources/b/docker-compose-pgadmin.yml",
                "src/test/resources/b/docker-compose-adminer.yml"));
        dockerComposeContainer.start();
    }
}
