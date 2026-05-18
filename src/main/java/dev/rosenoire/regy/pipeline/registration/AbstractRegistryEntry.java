package dev.rosenoire.regy.pipeline.registration;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NonNull;

public abstract class AbstractRegistryEntry<T, K> extends AbstractSimpleEntry<T> {
    protected final ResourceKey<K> resourceKey;

    public AbstractRegistryEntry(@NonNull T value, @NonNull ResourceKey<K> resourceKey) {
        super(value, resourceKey.identifier().withPrefix(resourceKey.registry().getPath() + "/"), resourceKey.identifier());
        this.resourceKey = resourceKey;
    }

    public ResourceKey<K> getResourceKey() {
        return resourceKey;
    }
}
