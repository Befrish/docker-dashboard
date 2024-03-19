package de.befrish.docker.dashboard.domain;

import java.util.Optional;

public interface ContainerApplicationData {

    Optional<String> getApplicationUrl();

    Optional<String> getApplicationVersionUrl();

    Optional<String> getApplicationVersionFormat();

}
