package de.befrish.docker.dashboard.domain;

import reactor.core.publisher.Mono;

public interface ContainerControl {

    Mono<Boolean> isRunning();

    Mono<Void> start();

    Mono<Void> stop();

    Mono<Void> restart();

}
