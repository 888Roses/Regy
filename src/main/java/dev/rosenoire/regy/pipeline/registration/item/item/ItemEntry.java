package dev.rosenoire.regy.pipeline.registration.item.item;

import dev.rosenoire.regy.pipeline.registration.AbstractRegistryEntry;
import dev.rosenoire.regy.pipeline.registration.item.group.CreativeTabGroup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ItemEntry<I extends Item> extends AbstractRegistryEntry<I, Item> implements ItemLike {
    public final @Nullable ToolMaterial toolMaterial;
    public final CreativeTabGroup tabGroup;

    public ItemEntry(@NonNull I value, ResourceKey<Item> resourceKey, @Nullable ToolMaterial toolMaterial, CreativeTabGroup tabGroup) {
        super(value, resourceKey);
        this.toolMaterial = toolMaterial;
        this.tabGroup = tabGroup;
    }

    @Override
    public @NonNull Item asItem() {
        return get();
    }
}
