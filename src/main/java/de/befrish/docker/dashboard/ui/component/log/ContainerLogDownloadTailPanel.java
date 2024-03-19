package de.befrish.docker.dashboard.ui.component.log;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import de.befrish.docker.dashboard.domain.HasContainerId;
import de.befrish.docker.dashboard.service.ContainerLogResolver;
import lombok.NonNull;
import org.vaadin.olli.FileDownloadWrapper;

public class ContainerLogDownloadTailPanel extends FormLayout {

    private static final int DEFAULT_LINE_COUNT = 1000;
    public static final int MIN_LINE_COUNT = 1;

    public ContainerLogDownloadTailPanel(
            @NonNull final HasContainerId container,
            @NonNull final ContainerLogResolver containerLogResolver) {
        final var lineCountField = new IntegerField("Logzeilen");
        lineCountField.setMin(MIN_LINE_COUNT);
        lineCountField.setValue(DEFAULT_LINE_COUNT);
        add(lineCountField);

        final var downloadButton = new Button("Download");
        final FileDownloadWrapper downloadWrapper = new ContainerLogFileDownloadWrapper(
                () -> {
                    final int lineCount = lineCountField.getOptionalValue()
                            .map(value -> Math.max(value, MIN_LINE_COUNT))
                            .orElse(DEFAULT_LINE_COUNT);
                    return containerLogResolver.resolveTail(container, lineCount).block();
                },
                () -> "docker_%d.log".formatted(System.currentTimeMillis())
        );
        downloadWrapper.wrapComponent(downloadButton);
        add(downloadWrapper);
    }

}
