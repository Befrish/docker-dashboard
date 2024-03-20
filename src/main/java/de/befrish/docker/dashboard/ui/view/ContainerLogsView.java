package de.befrish.docker.dashboard.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import de.befrish.docker.dashboard.domain.HasContainerId;
import de.befrish.docker.dashboard.domain.HasContainerName;
import de.befrish.docker.dashboard.reactor.LinesEmitter;
import de.befrish.docker.dashboard.service.ContainerIdResolver;
import de.befrish.docker.dashboard.service.ContainerLogResolver;
import de.befrish.docker.dashboard.ui.component.log.ContainerLogDownloadSincePanel;
import de.befrish.docker.dashboard.ui.component.log.ContainerLogDownloadTailPanel;
import de.befrish.docker.dashboard.ui.component.log.ContainerLogPanel;
import lombok.NonNull;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Route(value = "containers/:containerName/logs", layout = MainLayout.class)
public class ContainerLogsView extends VerticalLayout implements BeforeEnterObserver {

    private static final int LAST_LINES_COUNT = 300;

    private final ContainerIdResolver containerIdResolver;
    private final ContainerLogResolver containerLogResolver;
    private final H2 header;
    private final ContainerLogPanel containerLogPanel;

    private final VerticalLayout containerLogDownloadTailPanelContainer;

    private final VerticalLayout containerLogDownloadSincePanelContainer;

    public ContainerLogsView(
            @NonNull final ContainerIdResolver containerIdResolver,
            @NonNull final ContainerLogResolver containerLogResolver) {
        this.containerIdResolver = containerIdResolver;
        this.containerLogResolver = containerLogResolver;

        header = new H2();
        header.addClassNames("py-0", "px-m");
        add(header);

        containerLogPanel = new ContainerLogPanel();

        containerLogDownloadTailPanelContainer = new VerticalLayout();
        containerLogDownloadTailPanelContainer.setWidthFull();

        containerLogDownloadSincePanelContainer = new VerticalLayout();
        containerLogDownloadSincePanelContainer.setWidthFull();

        final VerticalLayout downloadLayout = new VerticalLayout(
                containerLogDownloadTailPanelContainer,
                containerLogDownloadSincePanelContainer);

        final var splitLayout = new SplitLayout(containerLogPanel, downloadLayout);
        splitLayout.setSplitterPosition(70);
        splitLayout.setWidthFull();
        addAndExpand(splitLayout);

        setSizeFull();
    }

    @Override
    public void beforeEnter(final BeforeEnterEvent event) {
        final Optional<String> containerName = event.getRouteParameters().get("containerName");
        containerName.ifPresent(containerName1 -> {
            updateHeader(() -> containerName1);

            final UI ui = UI.getCurrent();
            containerIdResolver.resolve(containerName1)
                    .subscribe(containerId -> ui.access(() -> {
                        updateLogs(() -> containerId, LAST_LINES_COUNT);
                        updateDownload(() -> containerId);
                    }));
        });
    }

    private void updateHeader(final HasContainerName container) {
        header.setText(
            Optional.ofNullable(container)
                    .map(HasContainerName::getContainerName)
                    .map("Logs für Container %s"::formatted)
                    .orElse("Kein Container gewählt") // TODO alternativ Fehlerseite mit 404 Not Found
        );
    }

    private void updateLogs(final HasContainerId container, final int lastLinesCount) {
        final UI ui = UI.getCurrent();
        Optional.ofNullable(container)
                .map(container1 -> containerLogResolver.resolveTail(container1, lastLinesCount))
                .ifPresentOrElse(
                        logInputStreamMono -> {
                            final Flux<String> logLinesFlux = logInputStreamMono.flux()
                                    .flatMap(logInputStream -> Flux.create(new LinesEmitter(logInputStream)));
                            logLinesFlux.subscribe(logLine -> ui.access(
                                    () -> containerLogPanel.addAndScrollTo(logLine)));
                        },
                        containerLogPanel::clear);
    }

    private void updateDownload(final HasContainerId container) {
        containerLogDownloadTailPanelContainer.removeAll();
        Optional.ofNullable(container)
                .ifPresent(container1 -> containerLogDownloadTailPanelContainer
                        .add(new ContainerLogDownloadTailPanel(container, containerLogResolver)));

        containerLogDownloadSincePanelContainer.removeAll();
        Optional.ofNullable(container)
                .ifPresent(container1 -> containerLogDownloadSincePanelContainer
                        .add(new ContainerLogDownloadSincePanel(container, containerLogResolver)));
    }

}
