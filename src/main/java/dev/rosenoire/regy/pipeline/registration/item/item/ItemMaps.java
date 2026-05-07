package dev.rosenoire.regy.pipeline.registration.item.item;

import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("deprecation")
@ApiStatus.NonExtendable
public interface ItemMaps {
    static <I extends Item, P> ItemEntryBuilder<I, P> singleStackSize(ItemEntryBuilder<I, P> entryBuilder) {
        return entryBuilder.properties(properties -> properties.stacksTo(1));
    }

    static <I extends Item, P> ItemEntryBuilder<I, P> sword(ItemEntryBuilder<I, P> entryBuilder) {
        return entryBuilder.tool(1, 2, false, (lookup, collector) -> {
            collector.accept(Tool.Rule.minesAndDrops(HolderSet.direct(Blocks.COBWEB.builtInRegistryHolder()), 15.0F));
            collector.accept(Tool.Rule.overrideSpeed(lookup.getOrThrow(BlockTags.SWORD_INSTANTLY_MINES), Float.MAX_VALUE));
            collector.accept(Tool.Rule.overrideSpeed(lookup.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5F));
        });
    }

    static <I extends Item, P> ItemEntryBuilder<I, P> tool(ItemEntryBuilder<I, P> entryBuilder, TagKey<Block> validBlocks, ToolMaterial material) {
        return entryBuilder.tool(1, 1, true, (lookup, collector) -> {
            collector.accept(Tool.Rule.deniesDrops(lookup.getOrThrow(material.incorrectBlocksForDrops())));
            collector.accept(Tool.Rule.minesAndDrops(lookup.getOrThrow(validBlocks), material.speed()));
        });
    }

    static <I extends Item, P> ItemEntryBuilder<I, P> pickaxe(ItemEntryBuilder<I, P> entryBuilder, ToolMaterial toolMaterial) {
        return tool(entryBuilder, BlockTags.MINEABLE_WITH_PICKAXE, toolMaterial);
    }

    static <I extends Item, P> ItemEntryBuilder<I, P> axe(ItemEntryBuilder<I, P> entryBuilder, ToolMaterial toolMaterial) {
        return tool(entryBuilder, BlockTags.MINEABLE_WITH_AXE, toolMaterial);
    }

    static <I extends Item, P> ItemEntryBuilder<I, P> hoe(ItemEntryBuilder<I, P> entryBuilder, ToolMaterial toolMaterial) {
        return tool(entryBuilder, BlockTags.MINEABLE_WITH_HOE, toolMaterial);
    }

    static <I extends Item, P> ItemEntryBuilder<I, P> shovel(ItemEntryBuilder<I, P> entryBuilder, ToolMaterial toolMaterial) {
        return tool(entryBuilder, BlockTags.MINEABLE_WITH_SHOVEL, toolMaterial);
    }
}