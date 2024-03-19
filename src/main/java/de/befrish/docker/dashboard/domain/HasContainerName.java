package de.befrish.docker.dashboard.domain;

import lombok.NonNull;

public interface HasContainerName {

    @NonNull
    String getContainerName();

}
