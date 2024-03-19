package de.befrish.docker.dashboard.ui.component.renderer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.ValueProvider;
import de.befrish.docker.dashboard.ui.component.AsyncUpdatableComponentContainer;
import de.befrish.docker.dashboard.ui.eventbus.UiEventBus;
import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.util.Objects;
import java.util.logging.Level;

public class AsyncFluxComponentRenderer<SOURCE> extends ComponentRenderer<Component, SOURCE> {

    public <T> AsyncFluxComponentRenderer(
            @NonNull final ValueProvider<SOURCE, Flux<T>> asyncValueProvider,
            @NonNull final SerializableFunction<T, ? extends Component> componentFunction,
            final Class<? extends ComponentEvent<?>> updateEventClass) {
        super(source -> {
            final var containerComponent = new AsyncUpdatableComponentContainer();
            final Flux<Component> asyncComponentProvider = asyncValueProvider.apply(source)
                    .<Component>map(componentFunction)
                    .log(AsyncFluxComponentRenderer.class.getName(), Level.WARNING, SignalType.ON_ERROR)
                    .onErrorResume(exception -> Flux.just(VaadinIcon.QUESTION_CIRCLE.create()));
            asyncComponentProvider.subscribe(containerComponent::updateAsync);
            if (Objects.nonNull(updateEventClass)) {
                UiEventBus.subscribe(
                        containerComponent,
                        updateEventClass,
                        event -> asyncComponentProvider.subscribe(containerComponent::updateAsync));
            }
            return containerComponent;
        });
    }

}
