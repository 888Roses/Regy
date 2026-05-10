package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.pipeline.registration.block.BlockEntry;
import dev.rosenoire.regy.pipeline.registration.block.BlockRenderMode;
import net.minecraft.world.level.block.*;

import static dev.rosenoire.regy.test.common.TestModCommon.REGY;

public interface AllBlocks {
    BlockEntry<Block> ELDEN_BLOCK = REGY
            .block("elden_block")
            .properties(Blocks.GOLD_BLOCK)
            .properties(p -> p.sound(SoundType.AMETHYST))
            .item()
            .recipe(AllRecipes::eldenIngotsToEldenBlock)
            .recipe(AllRecipes::eldenBlockToEldenIngots)
            .build()
            .register();

    BlockEntry<Block> ELDEN_TILES = REGY
            .block("elden_tiles")
            .properties(Blocks.GOLD_BLOCK)
            .properties(p -> p.sound(SoundType.AMETHYST))
            .simpleItem()
            .register();

    BlockEntry<RotatedPillarBlock> ELDEN_PILLAR = REGY
            .block("elden_pillar", RotatedPillarBlock::new)
            .properties(Blocks.GOLD_BLOCK)
            .properties(p -> p.sound(SoundType.AMETHYST))
            .simpleItem()
            .pillarModel()
            .register();

    BlockEntry<IronBarsBlock> ELDEN_BARS = REGY
            .block("elden_bars", IronBarsBlock::new)
            .properties(Blocks.IRON_BARS)
            .properties(p -> p.sound(SoundType.AMETHYST))
            .simpleItem()
            .barsModel()
            .cutout()
            .register();

    BlockEntry<ChainBlock> ELDEN_CHAINS = REGY
            .block("elden_chains", ChainBlock::new)
            .properties(Blocks.IRON_CHAIN)
            .properties(p -> p.sound(SoundType.AMETHYST))
            .simpleItem()
            .chainsModel()
            .cutout()
            .register();

    static void register() {
    }
}
