package dev.rosenoire.regy.common.index;

import dev.rosenoire.regy.pipeline.registration.tag.block.BlockTagEntry;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.ApiStatus;

import static dev.rosenoire.regy.common.InternalRegyCommon.INTERNAL_REGY;

@ApiStatus.Internal
@ApiStatus.NonExtendable
public interface InternalBlockTags {
    BlockTagEntry SKIN_SWAPPER = INTERNAL_REGY
            .blockTag("skin_swapper")
            .block(Blocks.SMITHING_TABLE)
            .register();

    @ApiStatus.Internal
    static void register() {
    }
}
