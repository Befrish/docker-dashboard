package de.befrish.docker.dashboard.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Project {

    @NonNull
    @EqualsAndHashCode.Include
    String name;

}
