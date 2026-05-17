package dev.rosenoire.regy.api.data;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@ApiStatus.NonExtendable
public interface DataUtils {
    static <A, B, L extends List<B>> void addToMapLister(Map<A, L> map, NonNullSupplier<L> supplier, A key, B value) {
        if (!map.containsKey(key)) {
            map.put(key, supplier.get());
        }

        map.get(key).add(value);
    }

    static <K, V> V getOrAddAndGetFallback(Map<K, V> map, K key, V fallback) {
        if (map.containsKey(key)) {
            return map.get(key);
        }

        map.put(key, fallback);
        return fallback;
    }

    static <A> int containedCount(List<A> a, List<A> b) {
        var i = 0;
        for (var v : a) for (var v2 : b) if (v.equals(v2)) i++;
        return i;
    }

    static <A> int containedCountInStrictOrder(List<A> a, List<A> b) {
        var i = 0;

        for (var v : a)
            for (var v2 : b) {
                if (v.equals(v2)) i++;
                else break;
            }

        return i;
    }
}