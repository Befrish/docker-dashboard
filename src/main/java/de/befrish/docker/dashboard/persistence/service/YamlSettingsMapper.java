package de.befrish.docker.dashboard.persistence.service;

import de.befrish.docker.dashboard.domain.Settings;
import de.befrish.docker.dashboard.persistence.model.YamlSettings;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Optional;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface YamlSettingsMapper {

    Settings map(YamlSettings yamlSettings);

    YamlSettings map(Settings settings);

    default String map(final Optional<String> value) {
        return value.orElse(null);
    }

}
