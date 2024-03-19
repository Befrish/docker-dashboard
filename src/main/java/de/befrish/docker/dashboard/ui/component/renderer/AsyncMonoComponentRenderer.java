package de.befrish.docker.dashboard.ui.component.renderer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.ValueProvider;
import de.befrish.docker.dashboard.ui.component.AsyncUpdatableComponentContainer;
import de.befrish.docker.dashboard.ui.eventbus.UiEventBus;
import lombok.NonNull;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class AsyncMonoComponentRenderer<SOURCE, T> extends ComponentRenderer<Component, SOURCE> {

    public AsyncMonoComponentRenderer(
            @NonNull final ValueProvider<SOURCE, Mono<T>> asyncValueProvider,
            @NonNull final SerializableFunction<T, ? extends Component> componentFactory,
            @NonNull final SerializableFunction<Throwable, ? extends Component> errorComponentFactory,
            final Class<? extends ComponentEvent<?>> updateEventClass) {
        super(source -> {
            final var containerComponent = new AsyncUpdatableComponentContainer();
            createAsyncComponentProvider(
                    source,
                    asyncValueProvider,
                    componentFactory,
                    errorComponentFactory
            ).subscribe(containerComponent::updateAsync);
            if (Objects.nonNull(updateEventClass)) {
                UiEventBus.subscribe(
                        containerComponent,
                        updateEventClass,
                        event -> createAsyncComponentProvider(
                                source,
                                asyncValueProvider,
                                componentFactory,
                                errorComponentFactory
                        ).subscribe(containerComponent::updateAsync));
            }
            return containerComponent;
        });
    }

    private static <SOURCE, T> Mono<Component> createAsyncComponentProvider(
            final SOURCE source,
            final ValueProvider<SOURCE, Mono<T>> asyncValueProvider,
            final SerializableFunction<T, ? extends Component> componentFactory,
            final SerializableFunction<Throwable, ? extends Component> errorComponentFactory) {
        return asyncValueProvider.apply(source)
                .<Component>map(componentFactory)
//                    .log(AsyncMonoComponentRenderer.class.getName(), Level.WARNING, SignalType.ON_ERROR)
                .onErrorResume(exception -> Mono.fromSupplier(() -> errorComponentFactory.apply(exception)));
    }

}
