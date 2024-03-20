package de.befrish.docker.dashboard.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import de.befrish.docker.dashboard.domain.Container;
import de.befrish.docker.dashboard.service.ContainerControlProvider;
import de.befrish.docker.dashboard.ui.component.action.ContainerActionsBar;
import de.befrish.docker.dashboard.ui.component.renderer.AsyncMonoComponentRenderer;
import de.befrish.docker.dashboard.ui.presenter.ProjectPresenter;
import lombok.NonNull;

import java.time.Duration;

public class ContainerGrid extends Grid<Container> {

    public static final Duration CACHE_DURATION = Duration.ofHours(1);

    public ContainerGrid(
            @NonNull final ProjectPresenter projectPresenter,
            @NonNull final ContainerControlProvider containerControlProvider) {

        addColumn(Container::getDisplayName)
                .setHeader("Name")
                .setAutoWidth(true)
                .setResizable(true);

        addColumn(Container::getContainerName)
                .setHeader("Container Name")
                .setAutoWidth(true)
                .setResizable(true);

        addColumn(new AsyncMonoComponentRenderer<>(
                container -> projectPresenter.getContainerImage(container)
                        .cache(CACHE_DURATION), // does not change so often - needs reload to update
                Span::new,
                exception -> VaadinIcon.QUESTION_CIRCLE.create(),
                ContainerUpdateRequestedEvent.class))
                .setHeader("Image")
                .setAutoWidth(true)
                .setResizable(true);

        addColumn(new AsyncMonoComponentRenderer<>(
                container -> projectPresenter.getContainerPorts(container)
                        .cache(CACHE_DURATION), // does not change so often - needs reload to update
                Span::new,
                exception -> VaadinIcon.QUESTION_CIRCLE.create(),
                ContainerUpdateRequestedEvent.class))
                .setHeader("Ports")
                .setAutoWidth(true)
                .setResizable(true);

        addColumn(new AsyncMonoComponentRenderer<>(
                projectPresenter::getContainerStatus,
                Span::new,
                exception -> VaadinIcon.QUESTION_CIRCLE.create(),
                ContainerUpdateRequestedEvent.class))
                .setHeader("Status")
                .setAutoWidth(true)
                .setResizable(true);

        addColumn(new AsyncMonoComponentRenderer<>(
                projectPresenter::getApplicationStatus,
                applicationStatus -> centeredHorizontally(new ApplicationStatusImage(() -> applicationStatus)),
                exception -> centeredHorizontally(VaadinIcon.QUESTION_CIRCLE.create()),
                ContainerUpdateRequestedEvent.class))
                .setHeader("App Status")
                .setWidth("120px")
                .setFlexGrow(0);

        addColumn(new AsyncMonoComponentRenderer<>(
                projectPresenter::getApplicationVersion,
                Span::new,
                exception -> VaadinIcon.QUESTION_CIRCLE.create(),
                ContainerUpdateRequestedEvent.class))
                .setHeader("App Version")
                .setAutoWidth(true)
                .setResizable(true);

        addColumn(new AsyncMonoComponentRenderer<>(
                projectPresenter::getContainerForActions,
                containerForActions -> new ContainerActionsBar(containerForActions, containerControlProvider),
                exception -> VaadinIcon.QUESTION_CIRCLE.create(),
                ContainerUpdateRequestedEvent.class))
                .setHeader("Actions")
                .setWidth("290px")
                .setFlexGrow(0);
    }

    private static Component centeredHorizontally(final Component component) {
        return new HorizontalLayout(FlexComponent.JustifyContentMode.CENTER, component);
    }

}
