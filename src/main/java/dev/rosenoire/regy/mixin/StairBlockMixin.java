package dev.rosenoire.regy.mixin;

import dev.rosenoire.regy.foundation.extensions.StairBlockExtension;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StairBlock.class)
public class StairBlockMixin implements StairBlockExtension {
    @Shadow @Final protected BlockState baseState;

    @Override
    public @NonNull BlockState regy$getBaseState() {
        return this.baseState;
    }
}
