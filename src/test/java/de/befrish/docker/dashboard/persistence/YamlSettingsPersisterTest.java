package de.befrish.docker.dashboard.persistence;

import de.befrish.docker.dashboard.domain.Settings;
import de.befrish.docker.dashboard.domain.SettingsContainer;
import de.befrish.docker.dashboard.domain.SettingsProject;
import de.befrish.docker.dashboard.persistence.service.YamlSettingsPersister;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import static de.befrish.docker.dashboard.persistence.YamlMatcher.yamlEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class YamlSettingsPersisterTest { // TODO implement

    private static final String SETTINGS_YAML = """
            projects:
              - name: "testproject"
                containers:
                  - displayName: "Umgebung 1"
                    containerName: "container42"
                    applicationUrl:
                    applicationVersionUrl:
                    applicationVersionFormat:
            """;

    @Test
    void saveSettingsToYaml() throws IOException {
        try(final Writer settingsWriter = new StringWriter()) {
            final YamlSettingsPersister settingsPersister = new YamlSettingsPersister();
            settingsPersister.save(createSettings(), settingsWriter);
            final String writtenSettingsYaml = settingsWriter.toString();

            assertThat(writtenSettingsYaml, is(yamlEqualTo(SETTINGS_YAML)));
        }
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

}