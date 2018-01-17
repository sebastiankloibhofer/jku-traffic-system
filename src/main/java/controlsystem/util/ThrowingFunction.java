package controlsystem.util;

import java.util.function.Function;

/**
 * Functional interface extension that enables lambda functions
 * that throw exception.
 *
 * @param <IN> The type of the only argument
 * @param <OUT> The type of the returned value
 */
@FunctionalInterface
public interface ThrowingFunction<IN, OUT> extends Function<IN, OUT> {

    @Override
    default OUT apply(IN in) {
        try {
            return tryApply(in);
        } catch (final Exception e) {
            // If the default accept method is invoked,
            // encapsulate exceptions into runtime exceptions
            // (a little bit hacky, but tryAccept method is to prefer anyway)
            throw new RuntimeException(e);
        }
    }

    OUT tryApply(IN in) throws Exception;
}
