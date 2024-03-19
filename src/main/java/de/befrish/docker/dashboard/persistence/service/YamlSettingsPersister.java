package de.befrish.docker.dashboard.persistence.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import de.befrish.docker.dashboard.domain.Settings;
import de.befrish.docker.dashboard.persistence.model.YamlSettings;
import de.befrish.docker.dashboard.service.SettingsPersister;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.io.Writer;

@RequiredArgsConstructor
public class YamlSettingsPersister implements SettingsPersister {

    @Override
    public void save(final Settings settings, final Writer writer) {
        try (final Writer settingsWriter = writer) {
            final ObjectMapper mapper = new YAMLMapper();
            final YamlSettingsMapper settingsMapper = Mappers.getMapper(YamlSettingsMapper.class);

            final YamlSettings yamlSettings = settingsMapper.map(settings);
            mapper.writeValue(settingsWriter, yamlSettings);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
