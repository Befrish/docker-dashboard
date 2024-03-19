package de.befrish.docker.dashboard.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContainerIdResolver {

    Mono<String> resolve(String containerName);

}
