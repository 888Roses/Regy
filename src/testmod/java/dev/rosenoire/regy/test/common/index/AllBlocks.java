package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.pipeline.Regy;
import dev.rosenoire.regy.pipeline.registration.block.BlockEntry;
import dev.rosenoire.regy.pipeline.registration.block.BlockEntryBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.*;

import static dev.rosenoire.regy.test.common.TestModCommon.REGY;

@SuppressWarnings("unused")
public interface AllBlocks {
    BlockEntry<Block> ELDEN_BLOCK = REGY
            .block("elden_block")
            .transform(AllBlocks::eldenProperties)
            .item()
            .tag(ItemTags.PIGLIN_LOVED)
            .recipe(AllRecipes::eldenIngotsToEldenBlock)
            .recipe(AllRecipes::eldenBlockToEldenIngots)
            .build()
            .register();

    BlockEntry<Block> ELDEN_TILES = REGY
            .block("elden_tiles")
            .transform(AllBlocks::eldenProperties)
            .item()
            .tag(ItemTags.PIGLIN_LOVED)
            .build()
            .register();

    BlockEntry<Block> ELDEN_TILE_STAIRS = REGY
            .stairs("elden_tile_stairs", ELDEN_TILES)
            .properties(ELDEN_TILES)
            .tag(BlockTags.STAIRS)
            .item()
            .tag(ItemTags.PIGLIN_LOVED)
            .tag(ItemTags.STAIRS)
            .build()
            .stairsModel()
            .register();

    BlockEntry<RotatedPillarBlock> ELDEN_PILLAR = REGY
            .block("elden_pillar", RotatedPillarBlock::new)
            .transform(AllBlocks::eldenProperties)
            .item()
            .tag(ItemTags.PIGLIN_LOVED)
            .build()
            .pillarModel()
            .register();

    BlockEntry<IronBarsBlock> ELDEN_BARS = REGY
            .block("elden_bars", IronBarsBlock::new)
            .properties(Blocks.IRON_BARS)
            .transform(AllBlocks::eldenSound)
            .tag(BlockTags.BARS)
            .item()
            .tag(ItemTags.PIGLIN_LOVED)
            .tag(ItemTags.BARS)
            .build()
            .barsModel()
            .cutout()
            .register();

    BlockEntry<ChainBlock> ELDEN_CHAINS = REGY
            .block("elden_chains", ChainBlock::new)
            .properties(Blocks.IRON_CHAIN)
            .transform(AllBlocks::eldenSound)
            .tag(BlockTags.CHAINS)
            .item()
            .tag(ItemTags.PIGLIN_LOVED)
            .tag(ItemTags.CHAINS)
            .build()
            .chainsModel()
            .cutout()
            .register();

    static void register() {
    }

    private static <B extends Block> BlockEntryBuilder<B, Regy> eldenProperties(BlockEntryBuilder<B, Regy> builder) {
        return builder.properties(Blocks.GOLD_BLOCK).transform(AllBlocks::eldenSound);
    }

    private static <B extends Block> BlockEntryBuilder<B, Regy> eldenSound(BlockEntryBuilder<B, Regy> builder) {
        return builder.properties(properties -> properties.sound(SoundType.AMETHYST));
    }
}
