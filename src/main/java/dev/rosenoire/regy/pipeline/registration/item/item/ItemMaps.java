package dev.rosenoire.regy.pipeline.registration.item.item;

import net.minecraft.core.HolderSet;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@SuppressWarnings("deprecation")
@ApiStatus.NonExtendable
public interface ItemMaps {
    static <I extends Item, P> ItemEntryBuilder<I, P> singleStackSize(ItemEntryBuilder<I, P> entryBuilder) {
        return entryBuilder.properties(properties -> properties.stacksTo(1));
    }

    static <I extends Item, P> ItemEntryBuilder<I, P> swordTool(ItemEntryBuilder<I, P> entryBuilder) {
        return entryBuilder.tool(blockRegistry -> new Tool(
                List.of( // Rules
                        Tool.Rule.minesAndDrops(HolderSet.direct(Blocks.COBWEB.builtInRegistryHolder()), 15.0F),
                        Tool.Rule.overrideSpeed(blockRegistry.getOrThrow(BlockTags.SWORD_INSTANTLY_MINES), Float.MAX_VALUE),
                        Tool.Rule.overrideSpeed(blockRegistry.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5F)
                ),
                1.0F, // Default mining speed
                2, // Damage per block
                false // Can destroy blocks in creative
        ));
    }
}