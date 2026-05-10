package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.registration.block.BlockEntry;
import dev.rosenoire.regy.pipeline.registration.block.BlockEntryBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.function.UnaryOperator;

import static dev.rosenoire.regy.test.common.TestModCommon.REGY;
import static dev.rosenoire.regy.pipeline.content.BlockTransformers.*;

@SuppressWarnings("unused")
public interface AllBlocks {
    BlockEntry<Block> ELDEN_BLOCK = REGY
            .block("elden_block")
            .transform(eldenProperties())
            .item()
            .tag(ItemTags.PIGLIN_LOVED)
            .recipe(AllRecipes::eldenIngotsToEldenBlock)
            .recipe(AllRecipes::eldenBlockToEldenIngots)
            .build()
            .properties(requiresCorrectToolForDrops())
            .register();

    BlockEntry<Block> ELDEN_TILES = REGY
            .block("elden_tiles")
            .transform(eldenProperties())
            .transform(taggedItem(ItemTags.PIGLIN_LOVED))
            .properties(requiresCorrectToolForDrops())
            .register();

    BlockEntry<StairBlock> ELDEN_TILE_STAIRS = REGY
            .stairs("elden_tile_stairs", ELDEN_TILES)
            .properties(ELDEN_TILES)
            .properties(requiresCorrectToolForDrops())
            .transform(taggedItem(ItemTags.PIGLIN_LOVED, ItemTags.STAIRS))
            .stairsModel()
            .register();

    BlockEntry<SlabBlock> ELDEN_TILE_SLAB = REGY
            .block("elden_tile_slab", SlabBlock::new)
            .properties(ELDEN_TILES)
            .tag(BlockTags.SLABS)
            .properties(requiresCorrectToolForDrops())
            .transform(taggedItem(ItemTags.PIGLIN_LOVED, ItemTags.SLABS))
            .slabModel(ELDEN_TILES)
            .register();

    BlockEntry<WallBlock> ELDEN_TILE_WALL = REGY
            .block("elden_tile_wall", WallBlock::new)
            .properties(ELDEN_TILES)
            .tag(BlockTags.WALLS)
            .transform(nonFullBlock())
            .properties(requiresCorrectToolForDrops())
            .transform(taggedItem(ItemTags.PIGLIN_LOVED, ItemTags.WALLS))
            .wallModel(ELDEN_TILES)
            .register();

    BlockEntry<RotatedPillarBlock> ELDEN_PILLAR = REGY
            .block("elden_pillar", RotatedPillarBlock::new)
            .transform(eldenProperties())
            .properties(requiresCorrectToolForDrops())
            .transform(taggedItem(ItemTags.PIGLIN_LOVED))
            .pillarModel()
            .register();

    BlockEntry<IronBarsBlock> ELDEN_BARS = REGY
            .block("elden_bars", IronBarsBlock::new)
            .properties(Blocks.IRON_BARS)
            .transform(eldenSound())
            .transform(nonFullBlock())
            .properties(requiresCorrectToolForDrops())
            .transform(taggedItem(ItemTags.PIGLIN_LOVED, ItemTags.BARS))
            .barsModel().cutout()
            .tag(BlockTags.BARS)
            .register();

    BlockEntry<ChainBlock> ELDEN_CHAINS = REGY
            .block("elden_chains", ChainBlock::new)
            .properties(Blocks.IRON_CHAIN)
            .transform(eldenSound())
            .transform(nonFullBlock())
            .properties(requiresCorrectToolForDrops())
            .tag(BlockTags.CHAINS)
            .transform(taggedItem(ItemTags.PIGLIN_LOVED, ItemTags.CHAINS))
            .chainsModel().cutout()
            .register();

    BlockEntry<WaterloggedTransparentBlock> ELDEN_GRATE = REGY
            .block("elden_grate", WaterloggedTransparentBlock::new)
            .transform(eldenProperties())
            .transform(nonFullBlock())
            .properties(requiresCorrectToolForDrops())
            .properties(properties -> properties.isViewBlocking(Blocks::never).isSuffocating(Blocks::never))
            .transform(taggedItem(ItemTags.PIGLIN_LOVED))
            .cutout()
            .register();

    BlockEntry<TrapDoorBlock> ELDEN_GRATEDOOR = REGY
            .trapdoor("elden_gratedoor", BlockSetType.COPPER)
            .transform(eldenProperties())
            .transform(nonFullBlock())
            .properties(requiresCorrectToolForDrops())
            .transform(taggedItem(ItemTags.PIGLIN_LOVED, ItemTags.TRAPDOORS))
            .register();

    BlockEntry<DoorBlock> ELDEN_DOOR = REGY
            .door("elden_door", BlockSetType.COPPER)
            .transform(eldenProperties())
            .transform(nonFullBlock())
            .properties(requiresCorrectToolForDrops())
            .transform(taggedItem(ItemTags.PIGLIN_LOVED, ItemTags.DOORS))
            .register();

    BlockEntry<WaterloggedTransparentBlock> GILDED_GLASS = REGY
            .block("gilded_glass", WaterloggedTransparentBlock::new)
            .transform(eldenProperties())
            .transform(nonFullBlock())
            .properties(requiresCorrectToolForDrops())
            .transform(taggedItem(ItemTags.PIGLIN_LOVED))
            .cutout()
            .register();

    static void register() {
    }

    private static <B extends Block, R extends AbstractRegy<R>> UnaryOperator<BlockEntryBuilder<B, R>> eldenProperties() {
        return builder -> builder
                .properties(Blocks.GOLD_BLOCK)
                .transform(eldenSound());
    }

    private static <B extends Block, R extends AbstractRegy<R>> UnaryOperator<BlockEntryBuilder<B, R>> eldenSound() {
        return builder -> builder.properties(properties -> properties.sound(SoundType.AMETHYST));
    }
}
