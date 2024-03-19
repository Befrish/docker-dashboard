package de.befrish.docker.dashboard.ui.component.log;

import com.vaadin.flow.component.ScrollOptions;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ContainerLogPanel extends VerticalLayout {

    public void addAndScrollTo(final String logLine) {
        final var logLineComponent = new Div(logLine);
        add(logLineComponent);
        logLineComponent.scrollIntoView(new ScrollOptions(
                ScrollOptions.Behavior.SMOOTH,
                ScrollOptions.Alignment.END,
                ScrollOptions.Alignment.START));
    }

    public void clear() {
        removeAll();
    }

}
