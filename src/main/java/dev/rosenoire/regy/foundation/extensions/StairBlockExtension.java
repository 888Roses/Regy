package dev.rosenoire.regy.foundation.extensions;

import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public interface StairBlockExtension {
    @NonNull BlockState regy$getBaseState();
}
