package de.befrish.docker.dashboard.dockerclient;

import de.befrish.docker.dashboard.domain.Container;
import de.befrish.docker.dashboard.domain.SettingsContainer;
import de.befrish.docker.dashboard.service.ContainerIdResolver;
import de.befrish.docker.dashboard.service.ProjectContainerMapper;
import de.befrish.docker.dashboard.service.SettingsContainerMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DockerClientProjectContainerMapper implements ProjectContainerMapper {

    @NonNull
    private final ContainerIdResolver containerIdResolver;

    @NonNull
    private final SettingsContainerMapper settingsContainerMapper;

    @Override
    public Mono<Container> map(final SettingsContainer settingsContainer) {
        return containerIdResolver.resolve(settingsContainer.getContainerName())
                .map(containerId -> settingsContainerMapper.map(settingsContainer, containerId));
    }

}
