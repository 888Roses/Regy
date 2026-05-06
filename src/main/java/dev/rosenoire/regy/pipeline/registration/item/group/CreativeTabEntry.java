package dev.rosenoire.regy.pipeline.registration.item.group;

import dev.rosenoire.regy.pipeline.registration.AbstractEntry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.jspecify.annotations.NonNull;

public class CreativeTabEntry extends AbstractEntry<CreativeModeTab, CreativeModeTab> {
    public CreativeTabEntry(@NonNull CreativeModeTab value, ResourceKey<CreativeModeTab> resourceKey) {
        super(value, resourceKey);
    }
}
