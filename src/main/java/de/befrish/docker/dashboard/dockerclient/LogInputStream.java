/*
 * Created: 24.06.2016
 * Copyright (c) 2005-2016 saxess ag. All rights reserved.
 */

package de.befrish.docker.dashboard.dockerclient;

import com.spotify.docker.client.LogStream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Wrapper, um {@link LogStream} als {@link InputStream} auslesen zu können.
 *
 * @author Benno Müller
 */
@RequiredArgsConstructor
public class LogInputStream extends InputStream {

    private static final int EOF = -1;

    @NonNull
    private final LogStream logStream;

    private ByteBuffer buffer;

    @Override
    public int read() {
        if (!bufferHasRemaining() && logStream.hasNext()) {
            buffer = logStream.next().content();
        }
        return bufferHasRemaining() ? buffer.get() : EOF;
    }

    private boolean bufferHasRemaining() {
        return buffer != null && buffer.hasRemaining();
    }

}
