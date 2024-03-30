package de.befrish.docker.dashboard.persistence;

import de.befrish.docker.dashboard.domain.Settings;
import de.befrish.docker.dashboard.domain.SettingsContainer;
import de.befrish.docker.dashboard.domain.SettingsProject;
import de.befrish.docker.dashboard.persistence.service.YamlSettingsResolver;
import de.befrish.docker.dashboard.service.SettingsResolver;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class YamlSettingsResolverTest {

    private static final String VALID_SETTINGS_YAML = """
            projects:
              - name: "testproject"
                containers:
                  - displayName: "Umgebung 1"
                    containerName: "container42"
            """;

    private static final String INVALID_SETTINGS_YAML = """
            projects:
              - name: "testproject"
                containers:
                  - displayNames: "Umgebung 1"
                  - containerName: "container42"
            """;

    @Test
    void canLoadSettingsFromValidYaml() {
        final SettingsResolver settingsResolver = new YamlSettingsResolver(() -> new StringReader(VALID_SETTINGS_YAML));

        final Optional<Settings> settings = settingsResolver.loadSetttings();

        assertThat(settings, is(Optional.of(createSettings())));
    }

    private static Settings createSettings() {
        return Settings.builder()
                .projects(List.of(createProject()))
                .build();
    }

    private static SettingsProject createProject() {
        return SettingsProject.builder()
                .name("testproject")
                .containers(List.of(
                        SettingsContainer.builder()
                                .displayName("Umgebung 1")
                                .containerName("container42")
                                .build()
                ))
                .build();
    }

    @Test
    void canNotLoadSettingsFromInvalidYaml() {
        final SettingsResolver settingsResolver = new YamlSettingsResolver(() -> new StringReader(INVALID_SETTINGS_YAML));

        final Optional<Settings> settings = settingsResolver.loadSetttings();

        assertThat(settings, is(Optional.empty()));
    }

    @Test
    void settingsFileIoException() {
        final Reader settingsReader = new Reader() {

            @Override
            public int read(final char[] cbuf, final int off, final int len) throws IOException {
                throw new IOException("expected");
            }

            @Override
            public void close() throws IOException {
                throw new IOException("expected");
            }
        };
        final SettingsResolver settingsResolver = new YamlSettingsResolver(() -> settingsReader);

        final Optional<Settings> settings = settingsResolver.loadSetttings();

        assertThat(settings, is(Optional.empty()));
    }

}