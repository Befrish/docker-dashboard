package de.befrish.docker.dashboard.ui.view;

import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import de.befrish.docker.dashboard.service.ContainerControlProvider;
import de.befrish.docker.dashboard.ui.component.ContainerGrid;
import de.befrish.docker.dashboard.ui.component.ContainerUpdateRequestedEvent;
import de.befrish.docker.dashboard.ui.eventbus.UiEventBus;
import de.befrish.docker.dashboard.ui.model.ProjectForView;
import de.befrish.docker.dashboard.ui.presenter.ProjectPresenter;
import de.befrish.docker.dashboard.ui.service.NameUrlMapper;
import lombok.NonNull;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Optional;

@Route(value = "projects", layout = MainLayout.class)
public class ProjectView extends VerticalLayout implements HasUrlParameter<String> {

    private static final String NO_PROJECT_TEXT = "<No project found>";
    private static final Duration UPDATE_PERIOD = Duration.ofSeconds(5);

    @NonNull
    private final NameUrlMapper nameUrlMapper;

    @NonNull
    private final ProjectPresenter projectPresenter;

    private final HtmlContainer projectName;
    private final ContainerGrid containerGrid;

    public ProjectView(
            @NonNull final NameUrlMapper nameUrlMapper,
            @NonNull final ProjectPresenter projectPresenter,
            @NonNull final ContainerControlProvider containerControlProvider) {
        this.nameUrlMapper = nameUrlMapper;
        this.projectPresenter = projectPresenter;

        projectName = new H2(NO_PROJECT_TEXT);
        projectName.addClassNames("py-0", "px-m");
        add(projectName);

        final var refreshButton = new Button("Refresh");
        refreshButton.addClickListener(event -> UiEventBus.publish(new ContainerUpdateRequestedEvent(this)));
        add(refreshButton);

        containerGrid = new ContainerGrid(projectPresenter, containerControlProvider);
        addAndExpand(containerGrid);

        final UI ui = UI.getCurrent();
        Flux.interval(UPDATE_PERIOD)
                .subscribe(ignored -> ui.access(() -> UiEventBus.publish(new ContainerUpdateRequestedEvent(this))));
    }

    @Override
    public void setParameter(final BeforeEvent beforeEvent, final String projectNameParameter) {
        Optional.ofNullable(projectNameParameter)
                .flatMap(projectNameParameter1 -> {
                    final String projectName = nameUrlMapper.mapFromUrlParameter(projectNameParameter);
                    return projectPresenter.findProject(projectName);
                })
                .ifPresent(this::setProject);
    }

    private void setProject(final ProjectForView project) {
        projectName.setText(project.getName());

        final UI ui = UI.getCurrent();
        project.getContainers().subscribe(containers -> ui.access(() -> containerGrid.setItems(containers)));
    }

}
