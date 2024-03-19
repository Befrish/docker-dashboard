package de.befrish.docker.dashboard.ui.component.log;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import de.befrish.docker.dashboard.domain.HasContainerId;
import de.befrish.docker.dashboard.service.ContainerLogResolver;
import lombok.NonNull;
import org.vaadin.olli.FileDownloadWrapper;

import java.time.*;
import java.util.Locale;

public class ContainerLogDownloadSincePanel extends FormLayout {

    public ContainerLogDownloadSincePanel(
            @NonNull final HasContainerId container,
            @NonNull final ContainerLogResolver containerLogResolver) {
        final var sinceField = new DateTimePicker("Logs ab:", getDefaultSince());
        sinceField.setLocale(Locale.GERMAN);
        sinceField.setStep(Duration.ofHours(1));
        sinceField.setAutoOpen(true);
        add(sinceField);

        final var downloadButton = new Button("Download");
        final FileDownloadWrapper downloadWrapper = new ContainerLogFileDownloadWrapper(
                () -> {
                    final Instant since = sinceField.getOptionalValue()
                            .map(ContainerLogDownloadSincePanel::localDateTimeToInstant)
                            .orElseGet(() -> localDateTimeToInstant(getDefaultSince()));
                    return containerLogResolver.resolveSince(container, since).block();
                },
                () -> "docker_%d.log".formatted(System.currentTimeMillis())
        );
        downloadWrapper.wrapComponent(downloadButton);
        add(downloadWrapper);
    }

    private static LocalDateTime getDefaultSince() {
        return LocalDate.now().atStartOfDay();
    }

    private static Instant localDateTimeToInstant(final LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

}
