package dev.rosenoire.regy.pipeline.registration.item.item;

import dev.rosenoire.regy.pipeline.registration.AbstractRegistryEntry;
import dev.rosenoire.regy.pipeline.registration.item.group.CreativeTabGroup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ItemEntry<I extends Item> extends AbstractRegistryEntry<I, Item> {
    public final @Nullable ToolMaterial toolMaterial;
    public final CreativeTabGroup tabGroup;
    public final List<TagKey<Item>> tags;

    public ItemEntry(@NonNull I value, ResourceKey<Item> resourceKey, @Nullable ToolMaterial toolMaterial, CreativeTabGroup tabGroup, List<TagKey<Item>> tags) {
        super(value, resourceKey);
        this.toolMaterial = toolMaterial;
        this.tabGroup = tabGroup;
        this.tags = tags;
    }
}
