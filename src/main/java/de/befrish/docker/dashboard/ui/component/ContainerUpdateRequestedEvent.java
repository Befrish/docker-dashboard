package de.befrish.docker.dashboard.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class ContainerUpdateRequestedEvent extends ComponentEvent<Component> {

    public ContainerUpdateRequestedEvent(final Component source) {
        super(source, false);
    }

}
