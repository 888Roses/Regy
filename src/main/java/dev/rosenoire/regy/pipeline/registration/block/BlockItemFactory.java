package dev.rosenoire.regy.pipeline.registration.block;

import dev.rosenoire.regy.pipeline.factory.ItemFactory;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntryBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public @FunctionalInterface interface BlockItemFactory<B extends Block, I extends Item> extends ItemFactory<I> {
    I blockItem(B block, Item.Properties properties);

    @Override
    default I bake(ItemEntryBuilder<I, ?> builder, Item.Properties properties) {
        return blockItem(null, properties);
    }
}
