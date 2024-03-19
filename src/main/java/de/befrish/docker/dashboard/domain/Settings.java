package de.befrish.docker.dashboard.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Settings {

    @NonNull
    private final List<SettingsProject> projects;

}
