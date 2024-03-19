package de.befrish.docker.dashboard.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SettingsProject {

    @NonNull
    @EqualsAndHashCode.Include
    String name;

    @NonNull
    List<SettingsContainer> containers;

}
