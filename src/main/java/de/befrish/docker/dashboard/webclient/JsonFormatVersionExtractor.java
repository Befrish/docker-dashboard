package de.befrish.docker.dashboard.webclient;

import com.jayway.jsonpath.JsonPath;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class JsonFormatVersionExtractor implements FormatSpecificVersionExtractor {

    private static final String JSON_FORMAT_PREFIX = "json:";

    @Override
    public boolean canExtractVersion(final String versionFormat) {
        return versionFormat != null && versionFormat.toLowerCase().startsWith(JSON_FORMAT_PREFIX);
    }

    @Override
    public String extractVersion(@NonNull final String versionString, @NonNull final String versionFormat) {
        return JsonPath.read(versionString, "$." + versionFormat.substring(JSON_FORMAT_PREFIX.length()));
    }

}
