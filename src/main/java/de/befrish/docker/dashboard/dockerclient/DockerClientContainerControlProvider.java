package de.befrish.docker.dashboard.dockerclient;

import de.befrish.docker.dashboard.domain.ContainerControl;
import de.befrish.docker.dashboard.domain.HasContainerId;
import de.befrish.docker.dashboard.service.ContainerControlProvider;
import de.befrish.docker.dashboard.service.ContainerStatusResolver;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DockerClientContainerControlProvider implements ContainerControlProvider {

    private final ContainerStatusResolver containerStatusResolver;
    private final ReactiveDockerClient dockerClient;

    @Override
    public Mono<ContainerControl> getContainerControl(final HasContainerId container) {
        return Mono.just(new DockerContainerControl(container, containerStatusResolver, dockerClient));
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class DockerContainerControl implements ContainerControl {

        private static final int SECONDS_TO_WAIT_BEFORE_KILLING = 30;

        private final HasContainerId container;
        private final ContainerStatusResolver containerStatusResolver;
        private final ReactiveDockerClient dockerClient;

        @Override
        public Mono<Boolean> isRunning() {
            return containerStatusResolver.resolveContainerStatusById(container)
                    .map(DockerContainerControl::isDockerContainerRunning);
        }

        private static boolean isDockerContainerRunning(final String dockerContainerStatus) {
            return dockerContainerStatus.startsWith("Up");
        }

        @Override
        public Mono<Void> start() {
            return dockerClient.startContainer(container);
        }

        @Override
        public Mono<Void> stop() {
            return dockerClient.stopContainer(container, SECONDS_TO_WAIT_BEFORE_KILLING);
        }

        @Override
        public Mono<Void> restart() {
            return dockerClient.restartContainer(container, SECONDS_TO_WAIT_BEFORE_KILLING);
        }
    }

}
