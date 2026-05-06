package dev.rosenoire.regy.pipeline.registration;

import dev.rosenoire.regy.api.data.NonNullSupplier;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NonNull;

public abstract class AbstractEntry<T, K> implements NonNullSupplier<T> {
    protected final @NonNull T value;
    protected final ResourceKey<K> resourceKey;

    public AbstractEntry(@NonNull T value, ResourceKey<K> resourceKey) {
        this.value = value;
        this.resourceKey = resourceKey;
    }

    @Override
    public @NonNull T get() {
        return this.value;
    }

    public ResourceKey<K> getResourceKey() {
        return resourceKey;
    }
}
