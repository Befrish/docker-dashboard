package de.befrish.docker.dashboard.service;

import de.befrish.docker.dashboard.domain.ContainerStatistic;
import de.befrish.docker.dashboard.domain.HasContainerId;
import reactor.core.publisher.Mono;

public interface ContainerStatisticResolver {

    Mono<ContainerStatistic> resolve(HasContainerId container);

}
