package de.befrish.docker.dashboard.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import de.befrish.docker.dashboard.domain.HasContainerId;
import de.befrish.docker.dashboard.service.ContainerStatisticResolver;
import de.befrish.docker.dashboard.ui.component.StatisticForm;
import lombok.NonNull;
import org.apache.commons.io.IOUtils;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Route(value = "containers/:containerId/stats", layout = MainLayout.class)
public class ContainerStatisticsView extends VerticalLayout implements BeforeEnterObserver {

    private final ContainerStatisticResolver containerStatisticResolver;

    private final H2 header;

    private StatisticForm statisticForm;

    public ContainerStatisticsView(@NonNull final ContainerStatisticResolver containerStatisticResolver) {
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
        final String containerId = event.getRouteParameters().get("containerId").orElse(null);
        final HasContainerId container = Optional.ofNullable(containerId)
                .<HasContainerId>map(containerId1 -> () -> containerId1)
                .orElse(null);
        updateHeader(container);
        updateStatistic(container);
    }

    private void updateHeader(final HasContainerId container) {
        header.setText(
            Optional.ofNullable(container)
                    .map(HasContainerId::getContainerId)
                    .map("Statistik für Container %s"::formatted) // TODO Container-Name beschreibender!
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
