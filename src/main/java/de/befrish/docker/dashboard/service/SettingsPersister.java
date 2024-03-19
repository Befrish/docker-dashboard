package de.befrish.docker.dashboard.service;

import de.befrish.docker.dashboard.domain.Settings;

import java.io.Writer;

public interface SettingsPersister {

    void save(Settings settings, Writer writer);

}
