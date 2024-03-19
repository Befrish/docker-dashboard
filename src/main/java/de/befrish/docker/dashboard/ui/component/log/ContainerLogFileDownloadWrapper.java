/*
 * Created: 18.05.2016
 * Copyright (c) 2005-2016 saxess ag. All rights reserved.
 */

package de.befrish.docker.dashboard.ui.component.log;

import com.vaadin.flow.server.StreamResource;
import lombok.NonNull;
import org.vaadin.olli.FileDownloadWrapper;

import java.util.function.Supplier;

/**
 * @author Benno Müller
 */
public class ContainerLogFileDownloadWrapper extends FileDownloadWrapper {

    public ContainerLogFileDownloadWrapper(
            @NonNull final ContainerLogProvider containerLogProvider,
            @NonNull final Supplier<String> fileNameSupplier) {
        super(new StreamResource(fileNameSupplier.get(), containerLogProvider::getLog));
    }

}
