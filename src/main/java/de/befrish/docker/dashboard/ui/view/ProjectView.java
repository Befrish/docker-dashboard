package de.befrish.docker.dashboard.ui.view;

import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import de.befrish.docker.dashboard.domain.SettingsProject;
import de.befrish.docker.dashboard.service.*;
import de.befrish.docker.dashboard.ui.component.ContainerGrid;
import de.befrish.docker.dashboard.ui.component.ContainerUpdateRequestedEvent;
import de.befrish.docker.dashboard.ui.eventbus.UiEventBus;
import de.befrish.docker.dashboard.ui.service.ContainerForActionsMapper;
import de.befrish.docker.dashboard.ui.service.NameUrlMapper;
import lombok.NonNull;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Optional;

@Route(value = "projects", layout = MainLayout.class)
public class ProjectView extends VerticalLayout implements HasUrlParameter<String> {

    private static final String NO_PROJECT_TEXT = "<No project found>";
    private static final Duration UPDATE_PERIOD = Duration.ofSeconds(5);

    private final NameUrlMapper nameUrlMapper;
    private final SettingsProjectResolver projectResolver;
    private final ProjectContainerMapper projectContainerMapper;

    private final HtmlContainer projectName;
    private final ContainerGrid containerGrid;

    public ProjectView(
            @NonNull final NameUrlMapper nameUrlMapper,
            @NonNull final SettingsProjectResolver projectResolver,
            @NonNull final ProjectContainerMapper projectContainerMapper,
            @NonNull final ContainerDataResolver containerDataResolver,
            @NonNull final ContainerStatusResolver containerStatusResolver,
            @NonNull final ContainerControlProvider containerControlProvider,
            @NonNull final ApplicationStatusResolver applicationStatusResolver,
            @NonNull final ApplicationVersionResolver applicationVersionResolver,
            @NonNull final ContainerForActionsMapper containerForActionsMapper) {
        this.nameUrlMapper = nameUrlMapper;
        this.projectResolver = projectResolver;
        this.projectContainerMapper = projectContainerMapper;

        projectName = new H2(NO_PROJECT_TEXT);
        projectName.addClassNames("py-0", "px-m");
        add(projectName);

        final var refreshButton = new Button("Refresh");
        refreshButton.addClickListener(event -> UiEventBus.publish(new ContainerUpdateRequestedEvent(this)));
        add(refreshButton);

        containerGrid = new ContainerGrid(
                containerDataResolver,
                containerStatusResolver,
                containerControlProvider,
                applicationStatusResolver,
                applicationVersionResolver,
                containerForActionsMapper);
        addAndExpand(containerGrid);

        final UI ui = UI.getCurrent();
        Flux.interval(UPDATE_PERIOD)
                .subscribe(ignored -> ui.access(() -> UiEventBus.publish(new ContainerUpdateRequestedEvent(this))));
    }

    @Override
    public void setParameter(final BeforeEvent beforeEvent, final String projectNameParameter) {
        Optional.ofNullable(projectNameParameter).ifPresent(projectNameParameter1 -> {
            final String projectName = nameUrlMapper.mapFromUrlParameter(projectNameParameter);
            final Optional<SettingsProject> project = projectResolver.findByName(projectName);
            project.ifPresent(this::setProject);
        });
    }

    private void setProject(final SettingsProject project) {
        projectName.setText(project.getName());

        final UI ui = UI.getCurrent();
        Flux.fromIterable(project.getContainers())
                .flatMap(projectContainerMapper::map)
                .collectList()
                .subscribe(containers -> ui.access(() -> containerGrid.setItems(containers)));
    }

}
