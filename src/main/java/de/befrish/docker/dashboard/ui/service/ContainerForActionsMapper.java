package de.befrish.docker.dashboard.ui.service;

import de.befrish.docker.dashboard.domain.Container;
import de.befrish.docker.dashboard.domain.ContainerControl;
import de.befrish.docker.dashboard.service.ContainerControlProvider;
import de.befrish.docker.dashboard.ui.component.action.ContainerForActions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ContainerForActionsMapper {

    private final ContainerControlProvider containerControlProvider;

    public Mono<ContainerForActions> map(final Container container) {
        return containerControlProvider.getContainerControl(container)
                .flatMap(ContainerControl::isRunning)
                .map(running -> ContainerForActions.builder()
                        .containerId(container.getContainerId())
                        .containerName(container.getContainerName())
                        .applicationUrl(container.getApplicationUrl().orElse(null))
                        .running(running)
                        .build());
    }

}
