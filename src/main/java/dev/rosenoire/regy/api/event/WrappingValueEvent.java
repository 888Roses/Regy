package dev.rosenoire.regy.api.event;

import dev.rosenoire.regy.api.data.NonNullType;

public record WrappingValueEvent<@NonNullType V>(ValueEvent<V> before, ValueEvent<V> after) {
    public static <@NonNullType V> WrappingValueEvent<V> create() {
        return new WrappingValueEvent<>(new ValueEvent<>(), new ValueEvent<>());
    }
}
