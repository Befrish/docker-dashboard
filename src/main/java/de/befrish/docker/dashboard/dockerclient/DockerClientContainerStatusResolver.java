package de.befrish.docker.dashboard.dockerclient;

import com.spotify.docker.client.messages.Container;
import de.befrish.docker.dashboard.domain.HasContainerId;
import de.befrish.docker.dashboard.service.ContainerStatusResolver;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DockerClientContainerStatusResolver implements ContainerStatusResolver {

    @NonNull
    private DockerContainerResolver dockerContainerResolver;

    @Override
    public Mono<String> resolveContainerStatusById(final HasContainerId container) {
        return dockerContainerResolver.resolveById(container.getContainerId())
                .map(Container::status);
    }

}
