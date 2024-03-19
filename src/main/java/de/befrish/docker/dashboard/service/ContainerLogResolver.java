package de.befrish.docker.dashboard.service;

import de.befrish.docker.dashboard.domain.HasContainerId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.time.Instant;

/**
 * Stellt Logs von Docker Container bereit.
 *
 * @author Benno Müller
 */
public interface ContainerLogResolver {

    /**
     * Gibt Logs von Docker Container zurück.
     *
     * @param container Docker-ID des Containers
     * @param lastLinesCount Anzahl der letzten Zeilen, welche geladen werden (Maximum)
     *
     * @return Logs
     */
    Mono<InputStream> resolveTail(HasContainerId container, int lastLinesCount);

    /**
     * Gibt Logs von Docker Container zurück.
     *
     * @param container Docker-ID des Containers
     * @param lastLinesCount Anzahl der letzten Zeilen, welche geladen werden (Maximum)
     *
     * @return Logs
     */
    Flux<String> resolveTailLines(HasContainerId container, int lastLinesCount);

    /**
     * Gibt Logs von Docker Container seit einem bestimmten Zeitpunkt zurück.
     *
     * @param container Docker-ID des Containers
     * @param since Zeitstempel ab
     *
     * @return Logs
     */
    Mono<InputStream> resolveSince(HasContainerId container, Instant since);

}
