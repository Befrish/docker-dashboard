package de.befrish.docker.dashboard.service;

import de.befrish.docker.dashboard.domain.Container;
import de.befrish.docker.dashboard.domain.SettingsContainer;
import org.springframework.stereotype.Service;

@Service
public class SettingsContainerMapper {

    public Container map(final SettingsContainer container, final String containerId) {
        return Container.builder()
                .containerId(containerId)
                .containerName(container.getContainerName())
                .displayName(container.getDisplayName())
                .applicationUrl(container.getApplicationUrl().orElse(null))
                .applicationVersionUrl(container.getApplicationVersionUrl().orElse(null))
                .applicationVersionFormat(container.getApplicationVersionFormat().orElse(null))
                .build();
    }

}
