package dev.rosenoire.regy.api.data;

import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public @FunctionalInterface interface NonNullConsumer<@NonNullType V> extends Consumer<@NonNull V> {
    @Override
    void accept(@NonNull V v);

    static <@NonNullType V> @NonNull NonNullConsumer<V> ofConsumer(@NonNull Consumer<@NonNull V> consumer) {
        return value -> NonNullType.Validator.validate(
                value,
                () -> "Received null value from getter of NonNullSupplier. This is not allowed."
        );
    }
}
