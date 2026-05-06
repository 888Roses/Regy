package dev.rosenoire.regy.pipeline.factory;

import net.minecraft.world.item.Item;

@FunctionalInterface
public interface ItemFactory<I extends Item> {
    I bake(Item.Properties properties);
}
