package de.befrish.docker.dashboard.ui.eventbus;

@FunctionalInterface
@SuppressWarnings("rawtypes")
public interface ComponentEventSubscriber<T> {

    void onComponentEvent(T componentEvent);

}
