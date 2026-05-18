package dev.rosenoire.regy.pipeline.registration.block;

import dev.rosenoire.regy.pipeline.registration.AbstractRegistryEntry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.HashSet;

public class BlockEntry<B extends Block> extends AbstractRegistryEntry<B, Block> {
    public final BlockRenderMode renderMode;
    public final HashSet<TagKey<Block>> tagStorage;

    public BlockEntry(@NonNull B value, ResourceKey<Block> resourceKey, BlockRenderMode renderMode, HashSet<TagKey<Block>> tagStorage) {
        super(value, resourceKey);
        this.renderMode = renderMode;
        this.tagStorage = tagStorage;
    }
}
