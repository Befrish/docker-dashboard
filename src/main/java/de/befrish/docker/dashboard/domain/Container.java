package de.befrish.docker.dashboard.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.Optional;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Container implements HasContainerId, HasContainerName, ContainerApplicationData {

    @NonNull
    @EqualsAndHashCode.Include
    private final String containerId;

    @NonNull
    String containerName;

    @NonNull
    String displayName;

    String applicationUrl;

    String applicationVersionUrl;

    String applicationVersionFormat;

    public Optional<String> getApplicationUrl() {
        return Optional.ofNullable(applicationUrl);
    }

    public Optional<String> getApplicationVersionUrl() {
        return Optional.ofNullable(applicationVersionUrl);
    }

    public Optional<String> getApplicationVersionFormat() {
        return Optional.ofNullable(applicationVersionFormat);
    }

}
