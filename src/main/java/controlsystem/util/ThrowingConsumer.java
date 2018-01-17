package controlsystem.util;

import java.util.function.Consumer;

/**
 * Functional interface extension that enables lambda functions
 * that throw exception.
 *
 * @param <IN> The type of the only argument
 */
@FunctionalInterface
public interface ThrowingConsumer<IN> extends Consumer<IN> {

    @Override
    default void accept(IN in) {
        try {
            tryAccept(in);
        } catch (final Exception e) {
            // If the default accept method is invoked,
            // encapsulate exceptions into runtime exceptions
            // (a little bit hacky, but tryAccept method is to prefer anyway)
            throw new RuntimeException(e);
        }
    }

    void tryAccept(IN in) throws Exception;
}
