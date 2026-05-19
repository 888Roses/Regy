package dev.rosenoire.regy.api;

import java.util.Collection;

public class MathUtils {
    public static int secs2ticks(float seconds) {
        return Math.round(seconds * 20f);
    }

    public static int wrapInt(int i, int min, int max) {
        if (i < min) {
            i = max;
        }

        if (i > max) {
            i = min;
        }

        return i;
    }

    public static <E> int wrapInCollection(int i, Collection<E> collection) {
        return wrapInCollection(i, collection.size());
    }

    public static <E> int wrapInArray(int i, E[] array) {
        return wrapInCollection(i, array.length);
    }

    public static <E> int wrapInCollection(int i, int size) {
        return wrapInt(i, 0, size - 1);
    }
}
