package de.befrish.docker.dashboard.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class ContainerData {

    @NonNull
    private String image;

    @NonNull
    private String ports;

}
