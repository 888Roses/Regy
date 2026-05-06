package dev.rosenoire.regy.api.data;

import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface RegistryUtils {
    static <T> String toDescriptionId(ResourceKey<T> resourceKey) {
        var identifier = resourceKey.identifier();
        return resourceKey.registry().getPath() + "." + identifier.getNamespace() + "." + identifier.getPath();
    }
}
