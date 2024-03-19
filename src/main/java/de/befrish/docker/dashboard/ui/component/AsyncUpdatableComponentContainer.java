package de.befrish.docker.dashboard.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AsyncUpdatableComponentContainer extends Div {

    private final UI ui;

    public AsyncUpdatableComponentContainer() {
        this(UI.getCurrent());
    }

    public void updateAsync(final Component component) {
        ui.access(() -> {
            removeAll();
            add(component);
        });
    }

}
