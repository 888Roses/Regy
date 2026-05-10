package dev.rosenoire.regy.pipeline.factory;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/// `FunctionalInterface` responsible for creating a new instance of a `B` extending
/// [Block] using the provided [BlockBehaviour.Properties].
///
/// @param <B> `B` extending [Block] representing the block created by this factory.
public @FunctionalInterface interface BlockFactory<B extends Block> {
    /// Creates a new instance of `B` using the given `properties`.
    ///
    /// @param properties properties to be applied to the created instance of `B`.
    ///
    /// @return The created instance.
    B bake(BlockBehaviour.Properties properties);
}
