package dev.rosenoire.regy.pipeline.registration;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

public class ItemEntry<I extends Item> extends AbstractEntry<I, Item> {
    public ItemEntry(@NonNull I value, ResourceKey<Item> resourceKey) {
        super(value, resourceKey);
    }
}
