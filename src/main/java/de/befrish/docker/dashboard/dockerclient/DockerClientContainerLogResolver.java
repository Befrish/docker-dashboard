package de.befrish.docker.dashboard.dockerclient;

import de.befrish.docker.dashboard.domain.HasContainerId;
import de.befrish.docker.dashboard.service.ContainerLogResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.time.Instant;

import static com.spotify.docker.client.DockerClient.LogsParam.*;

@Service
@RequiredArgsConstructor
public class DockerClientContainerLogResolver implements ContainerLogResolver {

    private final ReactiveDockerClient dockerClient;

    @Override
    public Mono<InputStream> resolveTail(final HasContainerId container, final int lastLinesCount) {
        return dockerClient.logInputStream(
                container,
                stdout(),
                stderr(),
                tail(lastLinesCount));
    }

    @Override
    public Flux<String> resolveTailLines(final HasContainerId container, final int lastLinesCount) {
        return dockerClient.logLines(
                container,
                stdout(),
                stderr(),
                tail(lastLinesCount));
    }

    @Override
    public Mono<InputStream> resolveSince(final HasContainerId container, final Instant since) {
        return dockerClient.logInputStream(
                container,
                stdout(),
                stderr(),
                since((int) since.getEpochSecond()));
    }

}
