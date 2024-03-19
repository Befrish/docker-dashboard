package de.befrish.docker.dashboard.reactor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.MonoSink;

import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class ItemEmitter<E> implements Consumer<MonoSink<E>> {

    private final ValueProvider<E> valueProvider;

    @Override
    public void accept(final MonoSink<E> emitter) {
        try {
            valueProvider.getValue().ifPresentOrElse(emitter::success, emitter::success);
        } catch (final Exception exception) {
            emitter.error(exception);
        }
    }

    @FunctionalInterface
    public interface ValueProvider<E> {
        @NonNull
        Optional<E> getValue() throws Exception;
    }

}
