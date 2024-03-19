/*
 * Created: 02.05.2016
 * Copyright (c) 2005-2016 saxess ag. All rights reserved.
 */

package de.befrish.docker.dashboard.webclient;

import de.befrish.docker.dashboard.domain.ApplicationStatus;
import de.befrish.docker.dashboard.domain.ContainerApplicationData;
import de.befrish.docker.dashboard.service.ApplicationStatusResolver;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Benno Müller
 */
@Service
@RequiredArgsConstructor
public class WebClientApplicationStatusResolver implements ApplicationStatusResolver {

    @NonNull
    private final WebClientProvider webClientProvider;

    @Override
    public Mono<ApplicationStatus> getApplicationStatus(final ContainerApplicationData containerApplicationData) {
        return containerApplicationData.getApplicationUrl()
                .map(this::requestStatus)
                .orElse(Mono.empty());
    }

    private Mono<ApplicationStatus> requestStatus(@NonNull final String applicationUrl) {
        return webClientProvider.createWebClient().get()
                .uri(applicationUrl)
                .retrieve()
                .toEntity(String.class)
                .map(ResponseEntity::getStatusCode)
                .map(WebClientApplicationStatusResolver::mapHttpStatusToApplicationStatus);
    }

    private static ApplicationStatus mapHttpStatusToApplicationStatus(final HttpStatusCode httpStatusCode) {
        return switch (httpStatusCode.value()) {
            case HttpStatus.SC_OK, HttpStatus.SC_MOVED_TEMPORARILY -> ApplicationStatus.ONLINE;
            default -> ApplicationStatus.OFFLINE;
        };
    }

}
