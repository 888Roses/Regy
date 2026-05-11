package dev.rosenoire.regy.foundation.extensions;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface BlockItemExtension {
    @Nullable BlockState regy$getPlacementState(BlockPlaceContext blockPlaceContext);
}
