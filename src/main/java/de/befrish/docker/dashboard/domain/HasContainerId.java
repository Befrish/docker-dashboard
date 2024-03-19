package de.befrish.docker.dashboard.domain;

import lombok.NonNull;

public interface HasContainerId {

    @NonNull
    String getContainerId();

}
