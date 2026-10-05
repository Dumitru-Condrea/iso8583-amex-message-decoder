package com.barclaycard.systemtest.emv.gateway.db.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityUtils {

    public static <T, V> Predicate<T> by(
            Function<T, V> getter,
            V value
    ) {
        return entity ->
                Objects.equals(
                        getter.apply(entity),
                        value
                );
    }

    public static <T, V extends Comparable<? super V>>
    Comparator<T> comparing(
            Function<T, V> getter
    ) {
        return Comparator.comparing(
                getter,
                Comparator.nullsLast(
                        Comparator.naturalOrder()
                )
        );
    }

    public static <T, V extends Comparable<? super V>>
    Comparator<T> comparingDescending(
            Function<T, V> getter
    ) {
        return Comparator.comparing(
                getter,
                Comparator.nullsLast(
                        Comparator.reverseOrder()
                )
        );
    }
}
