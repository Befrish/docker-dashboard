package de.befrish.docker.dashboard.ui.service;

import org.springframework.stereotype.Service;

@Service
public class NameUrlMapper {

    public String mapToUrlParameter(final String name) {
        return name.replaceAll(" ", "-");
    }

    public String mapFromUrlParameter(final String urlParameter) {
        return urlParameter.replaceAll("-", " ");
    }

}
