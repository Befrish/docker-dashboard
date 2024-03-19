package de.befrish.docker.dashboard.service;

import de.befrish.docker.dashboard.domain.ContainerControl;
import de.befrish.docker.dashboard.domain.HasContainerId;
import reactor.core.publisher.Mono;

public interface ContainerControlProvider {

    Mono<ContainerControl> getContainerControl(HasContainerId container);

}
