package dev.rosenoire.regy.pipeline.registration;

import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NonNull;

public abstract class AbstractRegistryEntry<T, K> extends AbstractSimpleEntry<T> {
    protected final ResourceKey<K> resourceKey;

    public AbstractRegistryEntry(@NonNull T value, ResourceKey<K> resourceKey) {
        super(value);
        this.resourceKey = resourceKey;
    }

    public ResourceKey<K> getResourceKey() {
        return resourceKey;
    }
}
