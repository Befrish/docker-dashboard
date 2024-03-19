package de.befrish.docker.dashboard.webclient;

import org.springframework.stereotype.Service;

@Service
public class TextFormatVersionExtractor implements FormatSpecificVersionExtractor {

    public static final String TEXT_FORMAT = "text";

    @Override
    public boolean canExtractVersion(final String versionFormat) {
        return versionFormat.toLowerCase().equals(TEXT_FORMAT);
    }

    @Override
    public String extractVersion(final String versionString, final String versionFormat) {
        return versionString;
    }

}
