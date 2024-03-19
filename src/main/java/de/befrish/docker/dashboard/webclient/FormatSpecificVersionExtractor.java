package de.befrish.docker.dashboard.webclient;

public interface FormatSpecificVersionExtractor {

    boolean canExtractVersion(String versionFormat);

    String extractVersion(String versionString, String versionFormat);

}
