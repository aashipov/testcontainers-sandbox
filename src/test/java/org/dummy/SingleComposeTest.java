package org.dummy;

import org.junit.BeforeClass;

import java.util.List;

import static org.dummy.TestUtils.build;

/**
 * Single docker-compose file with inheritance, environment showcase.
 */
public class SingleComposeTest extends ComposeTest {
    @BeforeClass
    public static void setUp() {
        dockerComposeContainer = build(List.of("src/test/resources/a/docker-compose.yml"));
        dockerComposeContainer.start();
    }
}
