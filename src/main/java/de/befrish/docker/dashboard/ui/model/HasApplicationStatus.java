package de.befrish.docker.dashboard.ui.model;

import de.befrish.docker.dashboard.domain.ApplicationStatus;

@FunctionalInterface
public interface HasApplicationStatus {

    ApplicationStatus getApplicationStatus();

}
