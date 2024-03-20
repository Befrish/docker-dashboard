package de.befrish.docker.dashboard.ui.component.action;

import de.befrish.docker.dashboard.domain.HasContainerId;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

@Value
@Builder
public class ContainerForActions implements HasContainerId {

    String containerId;

    String containerName;

    String applicationUrl;

    boolean running;

    public Optional<String> getApplicationUrl() {
        return Optional.ofNullable(applicationUrl);
    }

}
