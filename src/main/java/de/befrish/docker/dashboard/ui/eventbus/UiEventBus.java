package de.befrish.docker.dashboard.ui.eventbus;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.shared.Registration;
import lombok.NonNull;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public final class UiEventBus {

    private UiEventBus() {
        super();
    }

    public static void publish(final ComponentEvent<?> event) {
        ComponentUtil.fireEvent(UI.getCurrent(), event);
    }

    public static <T extends ComponentEvent<?>> void subscribe(
            @NonNull final Component componentEventSuvscriberComponent,
            @NonNull final Class<T> componentEventType,
            @NonNull final ComponentEventSubscriber<T> componentEventSubscriber
    ) {
        final var registrationReference = new AtomicReference<Registration>();
        componentEventSuvscriberComponent.addAttachListener(attachEvent -> {
            final Registration registration = ComponentUtil
                    .addListener(UI.getCurrent(), componentEventType, componentEventSubscriber::onComponentEvent);
            registrationReference.set(registration);
        });
        componentEventSuvscriberComponent.addDetachListener(detachEvent -> {
            Optional.ofNullable(registrationReference.get()).ifPresent(Registration::remove);
        });
    }

}
