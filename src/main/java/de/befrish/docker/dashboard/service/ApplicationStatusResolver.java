package de.befrish.docker.dashboard.service;

import de.befrish.docker.dashboard.domain.ApplicationStatus;
import de.befrish.docker.dashboard.domain.ContainerApplicationData;
import reactor.core.publisher.Mono;

public interface ApplicationStatusResolver {

    Mono<ApplicationStatus> getApplicationStatus(ContainerApplicationData containerApplicationData);

}
