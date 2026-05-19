package dev.rosenoire.regy.pipeline.registration.block;

import dev.rosenoire.regy.api.data.NonNullSupplier;
import dev.rosenoire.regy.pipeline.registration.AbstractRegistryEntry;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.function.Supplier;

public class BlockEntry<B extends Block> extends AbstractRegistryEntry<B, Block> {
    public final BlockRenderMode renderMode;
    public final HashSet<TagKey<Block>> tagStorage;

    private final @NonNull Supplier<@Nullable ItemEntry<? extends Item>> item;

    public BlockEntry(
            @NonNull B value,
            ResourceKey<Block> resourceKey,
            BlockRenderMode renderMode,
            HashSet<TagKey<Block>> tagStorage,
            @NonNull Supplier<@Nullable ItemEntry<? extends Item>> item
    ) {
        super(value, resourceKey);
        this.renderMode = renderMode;
        this.tagStorage = tagStorage;
        this.item = item;
    }

    public final @NonNull Optional<ItemEntry<? extends Item>> getItem() {
        return Optional.of(item).map(Supplier::get);
    }

    public final ItemEntry<? extends Item> getItemOrThrow() {
        return getItem().orElseThrow();
    }
}
