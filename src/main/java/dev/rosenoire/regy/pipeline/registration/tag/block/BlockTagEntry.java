package dev.rosenoire.regy.pipeline.registration.tag.block;

import dev.rosenoire.regy.pipeline.registration.tag.AbstractTagEntry;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public class BlockTagEntry extends AbstractTagEntry<Block> {
    public BlockTagEntry(@NonNull TagKey<Block> value, @NonNull Identifier regyIdentifier, @NonNull Identifier identifier) {
        super(value, regyIdentifier, identifier);
    }

    public boolean is(@NonNull BlockState blockState) {
        return blockState.is(this.get());
    }

    public boolean is(@NonNull Block block) {
        return this.is(block.defaultBlockState());
    }
}