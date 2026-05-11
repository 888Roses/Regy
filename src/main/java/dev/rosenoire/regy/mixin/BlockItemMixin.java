package dev.rosenoire.regy.mixin;

import dev.rosenoire.regy.foundation.extensions.BlockItemExtension;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin implements BlockItemExtension {
    @Shadow protected abstract @Nullable BlockState getPlacementState(BlockPlaceContext blockPlaceContext);

    @Override
    public @Nullable BlockState regy$getPlacementState(BlockPlaceContext blockPlaceContext) {
        return this.getPlacementState(blockPlaceContext);
    }
}
