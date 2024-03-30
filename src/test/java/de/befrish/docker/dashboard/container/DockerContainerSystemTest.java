package de.befrish.docker.dashboard.container;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.when;

@SpringBootTest // TODO eigentlich nur wegen BuildProperties da - aber eigentlich ist die Application nicht notwendig
@Testcontainers
class DockerContainerSystemTest {

    private static final String APPLICATION_IMAGE_NAME = "befrish/docker-dashboard";
    private static final int APPLICATION_PORT = 8080;

    @Autowired
    private BuildProperties buildProperties;

    @BeforeEach
    void setUpRestAssured() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
//    @Disabled
    void containerStarts() {
        try (final GenericContainer applicationContainer = new GenericContainer(
                "%s:%s".formatted(APPLICATION_IMAGE_NAME, buildProperties.getVersion()))
                .withExposedPorts(APPLICATION_PORT)
                .waitingFor(Wait.forLogMessage(".*Started DockerDashboardApplication in \\d+\\.\\d+ seconds.*", 1))) {
            applicationContainer.start();
            when().get("http://localhost:%d".formatted(applicationContainer.getMappedPort(APPLICATION_PORT)))
                    .then()
                    .statusCode(200);
        }
    }

}
