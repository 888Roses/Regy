package dev.rosenoire.regy.pipeline.registration;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public abstract class AbstractSimpleEntry<T> implements Entry<T>  {
    protected final @NonNull T value;
    protected final Identifier regyIdentifier;

    public AbstractSimpleEntry(@NonNull T value, Identifier regyIdentifier) {
        this.value = value;
        this.regyIdentifier = regyIdentifier;
    }

    @Override
    public @NonNull T get() {
        return this.value;
    }

    @Override
    public Identifier regyIdentifier() {
        return this.regyIdentifier;
    }
}
