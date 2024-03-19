package de.befrish.docker.dashboard.config;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DockerClientConfiguration {

    /**
     * User Manual: https://github.com/spotify/docker-client/blob/master/docs/user_manual.md
     *
     * @return DockerClient
     * @throws DockerCertificateException Fehler
     */
    @Bean
    DockerClient dockerClient() throws DockerCertificateException {
        // Version siehe https://docs.docker.com/engine/reference/api/docker_remote_api/
        return DefaultDockerClient.fromEnv().build();
    }

}
