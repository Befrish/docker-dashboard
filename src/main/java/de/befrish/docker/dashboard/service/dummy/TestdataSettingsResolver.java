package de.befrish.docker.dashboard.service.dummy;

import de.befrish.docker.dashboard.domain.Settings;
import de.befrish.docker.dashboard.domain.SettingsContainer;
import de.befrish.docker.dashboard.domain.SettingsProject;
import de.befrish.docker.dashboard.service.SettingsResolver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestdataSettingsResolver implements SettingsResolver { // TODO Dummy Daten

    @Override
    public Optional<Settings> loadSetttings() {
        return Optional.of(Settings.builder()
                .projects(List.of(project1(), project2())).build());
    }

    private static SettingsProject project1() {
        return SettingsProject.builder()
                .name("Testproject 1")
                .containers(List.of(
                        SettingsContainer.builder()
                                .displayName("Umgebung 42")
                                .containerName("resources_app42_1")
                                .applicationUrl("http://localhost:15001")
                                .build(),
                        SettingsContainer.builder()
                                .displayName("Umgebung 44")
                                .containerName("resources_app44_1")
                                .applicationUrl("http://localhost:15003") // Offline anzeigen (eigentlich 15002)
                                .build()
                ))
                .build();
    }

    private static SettingsProject project2() {
        return SettingsProject.builder()
                .name("Testproject 2")
                .containers(List.of(
                        SettingsContainer.builder()
                                .displayName("Umgebung 53")
                                .containerName("resources_app53_1")
                                .applicationUrl("http://localhost:16001")
                                .applicationVersionUrl("http://localhost:16001/version")
                                .build(),
                        SettingsContainer.builder()
                                .displayName("Umgebung 55")
                                .containerName("resources_app55_1")
                                .applicationUrl("http://localhost:16002")
                                .applicationVersionUrl("http://localhost:16001/actuator/info")
                                .applicationVersionFormat("json:build.version")
                                .build()
                ))
                .build();
    }

}
