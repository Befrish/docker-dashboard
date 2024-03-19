package de.befrish.docker.dashboard.persistence.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import de.befrish.docker.dashboard.domain.Settings;
import de.befrish.docker.dashboard.persistence.model.YamlSettings;
import de.befrish.docker.dashboard.service.SettingsResolver;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.IOException;
import java.io.Reader;
import java.util.Optional;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class YamlSettingsResolver implements SettingsResolver {

    private final Logger logger = LoggerFactory.getLogger(YamlSettingsResolver.class);

    private final Supplier<Reader> readerSupplier;

    @Override
    public Optional<Settings> loadSetttings() {
        try (final Reader settingsReader = readerSupplier.get()) {
            final ObjectMapper mapper = new YAMLMapper();
            final YamlSettingsMapper settingsMapper = Mappers.getMapper(YamlSettingsMapper.class);

            final YamlSettings yamlSettings = mapper.readValue(settingsReader, YamlSettings.class);
            return Optional.of(settingsMapper.map(yamlSettings));
        } catch (final IOException | YAMLException exception) {
            logger.error("Error on load settings from YAML", exception);
        }
        return Optional.empty();
    }

}
