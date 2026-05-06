package dev.rosenoire.regy.api.event;

import dev.rosenoire.regy.api.data.NonNullType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractEvent<@NonNullType E> {
    protected final List<E> subscribers = new ArrayList<>();

    public void subscribe(E subscriber) {
        subscribers.add(subscriber);
    }

    @SafeVarargs
    public final void subscribe(E... subscribers) {
        for (var subscriber : subscribers) {
            this.subscribe(subscriber);
        }
    }

    public void unsubscribe(E subscriber) {
        subscribers.remove(subscriber);
    }

    @SafeVarargs
    public final void unsubscribe(E... subscribers) {
        for (var subscriber : subscribers) {
            this.unsubscribe(subscriber);
        }
    }

    public void forEachSubscriber(Consumer<E> action) {
        for (var subscriber : this.subscribers) {
            action.accept(subscriber);
        }
    }
}
