package de.befrish.docker.dashboard.dockerclient;

import de.befrish.docker.dashboard.domain.ContainerStatistic;
import de.befrish.docker.dashboard.domain.HasContainerId;
import de.befrish.docker.dashboard.service.ContainerStatisticResolver;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DockerClientContainerStatisticResolver implements ContainerStatisticResolver {

    @NonNull
    private final ReactiveDockerClient dockerClient;

    @NonNull
    private final DockerClientContainerStatisticMapper containerStatisticMapper;

    @Override
    public Mono<ContainerStatistic> resolve(final HasContainerId container) {
        return dockerClient.stats(container)
                .map(containerStatisticMapper::map);
    }

}
