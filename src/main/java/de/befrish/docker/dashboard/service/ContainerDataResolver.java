package de.befrish.docker.dashboard.service;

import de.befrish.docker.dashboard.domain.ContainerData;
import de.befrish.docker.dashboard.domain.HasContainerId;
import reactor.core.publisher.Mono;

public interface ContainerDataResolver {

    Mono<ContainerData> getContainerDataById(HasContainerId container);

}
