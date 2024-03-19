package de.befrish.docker.dashboard.ui.component;

import com.vaadin.flow.component.html.Image;
import de.befrish.docker.dashboard.ui.model.HasApplicationStatus;

public class ApplicationStatusImage extends Image {

    public ApplicationStatusImage(final HasApplicationStatus container) {
        super("images/%s-icon.png".formatted(getOnlineString(container).toLowerCase()), getOnlineString(container));
    }

    private static String getOnlineString(final HasApplicationStatus container) {
        return switch (container.getApplicationStatus()) {
            case ONLINE -> "Online";
            case OFFLINE -> "Offline";
        };
    }

}
