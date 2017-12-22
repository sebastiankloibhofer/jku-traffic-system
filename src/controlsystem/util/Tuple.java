package controlsystem.util;

/**
 * Utility class for tuples.
 */
public final class Tuple {
    private Tuple() {}

    public static class T2<T> {
        public final T _0;
        public final T _1;
        public T2(T t0, T t1) { _0 = t0; _1 = t1; }
    }

    public static class T3<T> extends T2<T> {
        public final T _2;
        public T3(T t0, T t1, T t2) { super(t0, t1); _2 = t2; }
    }

    public static <T> T2<T> t2(T t0, T t1) { return new T2<>(t0, t1); }
    public static <T> T3<T> t2(T t0, T t1, T t2) { return new T3<>(t0, t1, t2); }
}
