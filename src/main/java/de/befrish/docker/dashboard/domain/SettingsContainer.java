package de.befrish.docker.dashboard.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.util.Optional;

@Value
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SettingsContainer implements HasContainerName, ContainerApplicationData {

    @NonNull
    @EqualsAndHashCode.Include
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
