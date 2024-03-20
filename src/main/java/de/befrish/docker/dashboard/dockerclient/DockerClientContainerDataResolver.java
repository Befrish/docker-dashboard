package de.befrish.docker.dashboard.dockerclient;

import de.befrish.docker.dashboard.domain.ContainerData;
import de.befrish.docker.dashboard.domain.HasContainerId;
import de.befrish.docker.dashboard.service.ContainerDataResolver;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DockerClientContainerDataResolver implements ContainerDataResolver {

    @NonNull
    private DockerContainerResolver dockerContainerResolver;

    public Mono<ContainerData> getContainerDataById(final HasContainerId container) {
        return dockerContainerResolver.resolveById(container.getContainerId())
                .map(container1 -> ContainerData.builder()
                        .image(container1.image())
                        .ports(container1.portsAsString())
                        .build());
    }

}
