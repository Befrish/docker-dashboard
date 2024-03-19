package de.befrish.docker.dashboard.service;

import de.befrish.docker.dashboard.domain.HasContainerId;
import reactor.core.publisher.Mono;

public interface ContainerStatusResolver {

    Mono<String> resolveContainerStatusById(HasContainerId container);

}
