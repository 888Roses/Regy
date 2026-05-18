package dev.rosenoire.regy.pipeline.registration;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public abstract class AbstractSimpleEntry<T> implements Entry<T>  {
    protected final @NonNull T value;
    protected final @NonNull Identifier regyIdentifier;
    protected final @NonNull Identifier identifier;

    public AbstractSimpleEntry(@NonNull T value, @NonNull Identifier regyIdentifier, @NonNull Identifier identifier) {
        this.value = value;
        this.regyIdentifier = regyIdentifier;
        this.identifier = identifier;
    }

    @Override
    public @NonNull T get() {
        return this.value;
    }

    @Override
    public @NonNull Identifier regyIdentifier() {
        return this.regyIdentifier;
    }

    @Override
    public @NonNull Identifier identifier() {
        return this.identifier;
    }
}
