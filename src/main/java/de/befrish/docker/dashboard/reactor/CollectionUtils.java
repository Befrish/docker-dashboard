package de.befrish.docker.dashboard.reactor;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class CollectionUtils {

    private CollectionUtils() {
        super();
    }

    public static <T> Stream<T> iteratorToOrderedStream(final Iterator<T> iterator) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED),
                false);
    }

}
