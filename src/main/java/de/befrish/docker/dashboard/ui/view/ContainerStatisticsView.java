package de.befrish.docker.dashboard.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import de.befrish.docker.dashboard.domain.HasContainerId;
import de.befrish.docker.dashboard.domain.HasContainerName;
import de.befrish.docker.dashboard.service.ContainerIdResolver;
import de.befrish.docker.dashboard.service.ContainerStatisticResolver;
import de.befrish.docker.dashboard.ui.component.StatisticForm;
import lombok.NonNull;

import java.util.Optional;

@Route(value = "containers/:containerName/stats", layout = MainLayout.class)
public class ContainerStatisticsView extends VerticalLayout implements BeforeEnterObserver {

    private final ContainerIdResolver containerIdResolver;
    private final ContainerStatisticResolver containerStatisticResolver;

    private final H2 header;
    private final StatisticForm statisticForm;

    public ContainerStatisticsView(
            @NonNull final ContainerIdResolver containerIdResolver,
            @NonNull final ContainerStatisticResolver containerStatisticResolver) {
        this.containerIdResolver = containerIdResolver;
        this.containerStatisticResolver = containerStatisticResolver;

        header = new H2();
        header.addClassNames("py-0", "px-m");
        add(header);

        statisticForm = new StatisticForm();
        addAndExpand(statisticForm);

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
                        updateStatistic(() -> containerId);
                    }));
        });
    }

    private void updateHeader(final HasContainerName container) {
        header.setText(
            Optional.ofNullable(container)
                    .map(HasContainerName::getContainerName)
                    .map("Statistik für Container %s"::formatted)
                    .orElse("Kein Container gewählt") // TODO alternativ Fehlerseite mit 404 Not Found
        );
    }

    private void updateStatistic(final HasContainerId container) {
        final UI ui = UI.getCurrent();
        Optional.ofNullable(container)
                .map(containerStatisticResolver::resolve)
                .ifPresentOrElse(
                        statisticMono -> statisticMono.subscribe(containerStatistic -> ui.access(
                                () -> statisticForm.setValue(containerStatistic))),
                        statisticForm::clear);
    }

}
