package dev.rosenoire.regy.api.event;

import dev.rosenoire.regy.api.data.NonNullType;

import java.util.function.Consumer;

public class ValueEvent<@NonNullType V> extends AbstractEvent<Consumer<V>> implements Consumer<V> {
    @Override
    public void accept(V value) {
        forEachSubscriber(subscriber -> subscriber.accept(value));
    }
}