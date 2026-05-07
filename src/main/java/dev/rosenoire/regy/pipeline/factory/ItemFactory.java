package dev.rosenoire.regy.pipeline.factory;

import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntryBuilder;
import net.minecraft.world.item.Item;

@FunctionalInterface
public interface ItemFactory<I extends Item> {
    I bake(ItemEntryBuilder<I, ?> builder, Item.Properties properties);
}