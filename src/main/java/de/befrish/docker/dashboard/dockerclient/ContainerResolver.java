package de.befrish.docker.dashboard.dockerclient;

import com.spotify.docker.client.messages.Container;
import reactor.core.publisher.Mono;

public interface ContainerResolver {

    Mono<Container> resolveById(String containerId);

    Mono<Container> resolveByName(String containerName);

}
