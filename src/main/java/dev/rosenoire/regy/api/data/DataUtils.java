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
}