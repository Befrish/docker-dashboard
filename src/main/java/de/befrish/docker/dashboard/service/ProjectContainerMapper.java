package de.befrish.docker.dashboard.service;

import de.befrish.docker.dashboard.domain.Container;
import de.befrish.docker.dashboard.domain.SettingsContainer;
import reactor.core.publisher.Mono;

public interface ProjectContainerMapper {

    Mono<Container> map(SettingsContainer settingsContainer);

}
