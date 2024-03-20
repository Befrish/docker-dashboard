package de.befrish.docker.dashboard.dockerclient;

import com.spotify.docker.client.messages.Container;
import de.befrish.docker.dashboard.service.ContainerIdResolver;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DockerClientContainerIdResolver implements ContainerIdResolver {

    @NonNull
    private final DockerContainerResolver dockerContainerResolver;

    @Override
    public Mono<String> resolve(final String containerName) {
        return dockerContainerResolver.resolveByName(containerName).map(Container::id);
    }

}
