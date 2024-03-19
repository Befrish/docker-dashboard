package de.befrish.docker.dashboard.reactor;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.FluxSink;

import java.util.Iterator;
import java.util.function.Consumer;

import static de.befrish.docker.dashboard.reactor.CollectionUtils.iteratorToOrderedStream;

@RequiredArgsConstructor
public class ItemsEmitter<E> implements Consumer<FluxSink<E>> {

    private final IteratorProvider<E> iteratorProvider;

    @Override
    public void accept(final FluxSink<E> emitter) {
        try {
            iteratorToOrderedStream(iteratorProvider.getIterator()).forEachOrdered(emitter::next);
        } catch (final Exception exception) {
            emitter.error(exception);
        }
    }

    @FunctionalInterface
    public interface IteratorProvider<E> {
        Iterator<E> getIterator() throws Exception;
    }

}
