package dev.rosenoire.regy.pipeline.registration.item.item;

import dev.rosenoire.regy.pipeline.registration.AbstractRegistryEntry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ItemEntry<I extends Item> extends AbstractRegistryEntry<I, Item> {
    public final @Nullable ToolMaterial toolMaterial;

    public ItemEntry(@NonNull I value, ResourceKey<Item> resourceKey, @Nullable ToolMaterial toolMaterial) {
        super(value, resourceKey);
        this.toolMaterial = toolMaterial;
    }
}
