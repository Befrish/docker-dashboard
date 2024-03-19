package de.befrish.docker.dashboard.ui.component.action;

import com.vaadin.flow.component.HasEnabled;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouteParameters;
import de.befrish.docker.dashboard.domain.ContainerControl;
import de.befrish.docker.dashboard.service.ContainerControlProvider;
import de.befrish.docker.dashboard.ui.component.ContainerUpdateRequestedEvent;
import de.befrish.docker.dashboard.ui.eventbus.UiEventBus;
import de.befrish.docker.dashboard.ui.view.ContainerLogsView;
import de.befrish.docker.dashboard.ui.view.ContainerStatisticsView;
import lombok.NonNull;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ContainerActionsBar extends HorizontalLayout {

    public ContainerActionsBar(
            @NonNull final Mono<ContainerForActions> container,
            @NonNull final ContainerControlProvider containerControlProvider) {

        final Button startButton = createActionButton(VaadinIcon.PLAY, "Starten");
        final Button stopButton = createActionButton(VaadinIcon.STOP, "Stoppen");
        final Button restartButton = createActionButton(VaadinIcon.REFRESH, "Neustarten");
        final Button showLogsButton = createActionButton(VaadinIcon.FILE_TEXT_O, "Zeige Logs");
        final Button showStatsButton = createActionButton(VaadinIcon.BAR_CHART, "Zeige Stats");

        add(startButton, stopButton, restartButton, showLogsButton, showStatsButton);

        final Collection<HasEnabled> disabledOnRunningComponents = List.of(startButton);
        final Collection<HasEnabled> enabledOnRunningComponents = List.of(stopButton, restartButton, showStatsButton);

        getStyle().set("display", "inline-block");

        List.of(startButton, stopButton, restartButton, showLogsButton, showStatsButton)
                .forEach(component -> component.setEnabled(false));

        final UI ui = UI.getCurrent();
        container.subscribe(container1 -> ui.access(() -> {
            startButton.addClickListener(event -> runContainerCommand(
                    container1,
                    ContainerControl::start,
                    containerControlProvider));
            stopButton.addClickListener(event -> runContainerCommand(
                    container1,
                    ContainerControl::stop,
                    containerControlProvider));
            restartButton.addClickListener(event -> runContainerCommand(
                    container1,
                    ContainerControl::restart,
                    containerControlProvider));

            showLogsButton.addClickListener(event -> ui.navigate(
                    ContainerLogsView.class,
                    new RouteParameters("containerId", container1.getContainerId())));
            showStatsButton.addClickListener(event -> ui.navigate(
                    ContainerStatisticsView.class,
                    new RouteParameters("containerId", container1.getContainerId())));

            enabledOnRunningComponents.forEach(component -> component.setEnabled(container1.isRunning()));
            disabledOnRunningComponents.forEach(component -> component.setEnabled(!container1.isRunning()));

            container1.getApplicationUrl().ifPresent(applicationUrl -> {
                final Anchor gotoApplicationLink = createAnchor(
                        VaadinIcon.EXTERNAL_LINK,
                        "Gehe zur Anwendung",
                        applicationUrl);
                add(gotoApplicationLink);
            });
        }));
    }

    private static Button createActionButton(final VaadinIcon icon, final String hintText) {
        final Button button = new Button(icon.create());
        button.setTooltipText(hintText);
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        button.getStyle().set("cursor", "pointer");
        return button;
    }

    private static Anchor createAnchor(final VaadinIcon icon, final String hintText, final String href) {
        final Anchor anchor = new Anchor();
        Optional.ofNullable(href).ifPresent(anchor::setHref);
        anchor.add(createActionButton(icon, hintText));
        anchor.setHeight(16, Unit.PIXELS);
        anchor.setTarget("_blank");
        return anchor;
    }

    private static void runContainerCommand(
            final ContainerForActions container,
            final Function<ContainerControl, Mono<Void>> containerCommand,
            final ContainerControlProvider containerControlProvider) {
        final UI ui = UI.getCurrent();
        containerControlProvider.getContainerControl(container)
                .subscribe(containerControl -> containerCommand.apply(containerControl)
                        .subscribe(ignored -> ui.access(() -> UiEventBus
                                .publish(new ContainerUpdateRequestedEvent(ui)))));
    }

}
