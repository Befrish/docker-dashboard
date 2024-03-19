package de.befrish.docker.dashboard.service;

import de.befrish.docker.dashboard.domain.SettingsProject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SettingsProjectResolver {

    @NonNull
    private final SettingsResolver settingsResolver;

    public Optional<SettingsProject> findByName(final String name) {
        return settingsResolver.loadSetttings()
                .flatMap(settings -> settings.getProjects().stream()
                .filter(project -> name.equalsIgnoreCase(project.getName()))
                .findFirst());
    }

}
