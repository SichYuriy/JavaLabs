package com.gmail.at.sichyuriyy.netcracker.lab03;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;

/**
 * Created by Yuriy on 25.02.2017.
 */
public class TestUtils {

    public static boolean equalContentCollections(Collection<?> expected, Collection<?> actual) {
        int expectedSize = (expected == null) ? 0 : expected.size();
        int actualSize = (actual == null) ? 0 : actual.size();
        if (expectedSize == 0 && actualSize == 0) {
            return true;
        }
        if (expectedSize != actualSize) {
            return false;
        }
        boolean equals;

        equals = actual.stream()
                .allMatch(expected::contains);
        if (equals) {
            equals = expected.stream()
                    .allMatch(actual::contains);
        }
        return equals;
    }

    public static <E>boolean equalContentCollections(Collection<? extends E> expected,
                                                     Collection<? extends E> actual,
                                                     EqualComparator<? super E> comparator) {
        int expectedSize = (expected == null) ? 0 : expected.size();
        int actualSize = (actual == null) ? 0 : actual.size();
        if (expectedSize == 0 && actualSize == 0) {
            return true;
        }
        if (expectedSize != actualSize) {
            return false;
        }
        boolean equals;

        equals = actual.stream()
                .allMatch(
                        obj1 -> expected.stream().anyMatch((obj2) -> comparator.equalValues(obj1, obj2))
                );
        if (equals) {
            equals = expected.stream()
                    .allMatch(
                            obj1 -> actual.stream().anyMatch((obj2) -> comparator.equalValues(obj1, obj2))
                    );
        }
        return equals;
    }

    public static <E>boolean equals(E obj1, E obj2, EqualComparator<? super E> comparator) {
        return (obj1 == obj2) || comparator.equalValues(obj1, obj2);
    }

    @FunctionalInterface
    public interface EqualComparator<E> {
        boolean equalValues(E obj1, E obj2);
    }


}
