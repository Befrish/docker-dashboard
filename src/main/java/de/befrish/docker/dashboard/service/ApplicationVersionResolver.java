package de.befrish.docker.dashboard.service;

import de.befrish.docker.dashboard.domain.ContainerApplicationData;
import reactor.core.publisher.Mono;

public interface ApplicationVersionResolver {

    Mono<String> getApplicationVersion(ContainerApplicationData containerApplicationData);

}
