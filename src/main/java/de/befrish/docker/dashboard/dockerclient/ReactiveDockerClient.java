package de.befrish.docker.dashboard.dockerclient;

import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerStats;
import de.befrish.docker.dashboard.domain.HasContainerId;
import de.befrish.docker.dashboard.reactor.ItemEmitter;
import de.befrish.docker.dashboard.reactor.ItemsEmitter;
import de.befrish.docker.dashboard.reactor.LinesEmitter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactiveDockerClient {

    private final DockerClient dockerClient;

    public Flux<Container> listContainers(final DockerClient.ListContainersParam... params) {
        return runAsyncAsFlux(() -> dockerClient.listContainers(params));
    }

    public Mono<Void> startContainer(final HasContainerId container) {
        return runAsyncAsMono(() -> {
            dockerClient.startContainer(container.getContainerId());
            return null;
        });
    }

    public Mono<Void> stopContainer(final HasContainerId container, final int secondsToWaitBeforeKilling) {
        return runAsyncAsMono(() -> {
            dockerClient.stopContainer(container.getContainerId(), secondsToWaitBeforeKilling);
            return null;
        });
    }

    public Mono<Void> restartContainer(final HasContainerId container, final int secondsToWaitBeforeKilling) {
        return runAsyncAsMono(() -> {
            dockerClient.restartContainer(container.getContainerId(), secondsToWaitBeforeKilling);
            return null;
        });
    }

    public Mono<LogStream> logs(final HasContainerId container, final DockerClient.LogsParam... params) {
        return runAsyncAsMono(() -> dockerClient.logs(container.getContainerId(), params));
    }

    public Mono<InputStream> logInputStream(final HasContainerId container, final DockerClient.LogsParam... params) {
        return logs(container, params).map(LogInputStream::new);
    }

    public Flux<String> logLines(final HasContainerId container, final DockerClient.LogsParam... params) {
        return logInputStream(container, params).flux()
                .flatMap(logInputStream -> Flux.create(new LinesEmitter(logInputStream)));
    }

    public Mono<ContainerStats> stats(final HasContainerId container) {
        return runAsyncAsMono(() -> dockerClient.stats(container.getContainerId()));
    }

    private static <T> Mono<T> runAsyncAsMono(final DockerCallback<T> callback) {
        return Mono.create(new ItemEmitter<>(() -> Optional.ofNullable(callback.call())));
    }

    private static <T> Flux<T> runAsyncAsFlux(final DockerCallback<? extends Iterable<T>> callback) {
        return Flux.create(new ItemsEmitter<>(() -> callback.call().iterator()));
    }

    @FunctionalInterface
    private interface DockerCallback<T> {
        T call() throws DockerException, InterruptedException;
    }

}
