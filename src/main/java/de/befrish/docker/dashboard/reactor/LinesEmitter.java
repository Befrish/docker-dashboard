package de.befrish.docker.dashboard.reactor;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class LinesEmitter extends ItemsEmitter<String> {

    public LinesEmitter(final InputStream inputStream) {
        super(() -> IOUtils.lineIterator(
                inputStream,
                StandardCharsets.UTF_8));
    }

}
