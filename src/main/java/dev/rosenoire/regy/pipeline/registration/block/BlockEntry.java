package dev.rosenoire.regy.pipeline.registration.block;

import dev.rosenoire.regy.pipeline.registration.AbstractRegistryEntry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

public class BlockEntry<B extends Block> extends AbstractRegistryEntry<B, Block> {
    public final BlockRenderMode renderMode;

    public BlockEntry(@NonNull B value, ResourceKey<Block> resourceKey, BlockRenderMode renderMode) {
        super(value, resourceKey);
        this.renderMode = renderMode;
    }
}
