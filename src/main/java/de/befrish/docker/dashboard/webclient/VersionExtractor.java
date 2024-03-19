package de.befrish.docker.dashboard.webclient;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VersionExtractor {

    @NonNull
    private final List<FormatSpecificVersionExtractor> formatSpecificVersionExtractors;

    Optional<String> extractVersion(final String versionString, final String versionFormat) {
        return formatSpecificVersionExtractors.stream()
                .filter(versionExtractor -> versionExtractor.canExtractVersion(versionFormat))
                .map(versionExtractor -> versionExtractor.extractVersion(versionString, versionFormat))
                .findAny();
    }

}
