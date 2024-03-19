package de.befrish.docker.dashboard.dockerclient;

import com.spotify.docker.client.messages.Container;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.spotify.docker.client.DockerClient.ListContainersParam.allContainers;
import static com.spotify.docker.client.DockerClient.ListContainersParam.filter;

@Service
@RequiredArgsConstructor
public class DockerClientContainerResolver implements ContainerResolver {

    @NonNull
    private ReactiveDockerClient dockerClient;

    @Override
    public Mono<Container> resolveById(final String containerId) {
        return dockerClient.listContainers(allContainers(), filter("id", containerId)).next();
    }

    @Override
    public Mono<Container> resolveByName(final String containerName) {
        return dockerClient.listContainers(allContainers())
                .filter(dockerContainer -> containerConfigMatchesContainer(containerName, dockerContainer))
                .next();
    }

    private static boolean containerConfigMatchesContainer(
            @NonNull final String containerName,
            @NonNull final Container dockerContainer) {
        return dockerContainer.names().stream()
                .map(DockerClientContainerResolver::convertDockerContainerName)
                .anyMatch(containerName::equals);
    }

    private static String convertDockerContainerName(final String name) {
        return name.replace("/", "");
    }

}
