public static <T, V extends Comparable<? super V>> Predicate<T> onOrAfter(
        Function<T, V> getter,
        V value
) {
    return entity -> {
        V actual = getter.apply(entity);
        return actual != null && actual.compareTo(value) >= 0;
    };
}

public static <T, V extends Comparable<? super V>> Predicate<T> onOrBefore(
        Function<T, V> getter,
        V value
) {
    return entity -> {
        V actual = getter.apply(entity);
        return actual != null && actual.compareTo(value) <= 0;
    };
}
