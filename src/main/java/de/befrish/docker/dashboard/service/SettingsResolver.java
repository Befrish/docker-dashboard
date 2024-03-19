package de.befrish.docker.dashboard.service;

import de.befrish.docker.dashboard.domain.Settings;

import java.io.Reader;
import java.util.Optional;

public interface SettingsResolver {

    Optional<Settings> loadSetttings();

}
