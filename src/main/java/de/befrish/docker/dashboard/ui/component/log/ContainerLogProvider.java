package de.befrish.docker.dashboard.ui.component.log;

import java.io.InputStream;

@FunctionalInterface
public interface ContainerLogProvider {

    InputStream getLog();

}
