package dev.rosenoire.regy.pipeline.registration;

import org.jspecify.annotations.NonNull;

public abstract class AbstractSimpleEntry<T> implements Entry<T>  {
    protected final @NonNull T value;

    public AbstractSimpleEntry(@NonNull T value) {
        this.value = value;
    }

    @Override
    public @NonNull T get() {
        return this.value;
    }
}
