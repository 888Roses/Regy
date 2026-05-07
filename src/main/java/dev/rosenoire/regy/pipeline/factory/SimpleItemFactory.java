package dev.rosenoire.regy.pipeline.factory;

import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntryBuilder;
import net.minecraft.world.item.Item;

@FunctionalInterface
public interface SimpleItemFactory<I extends Item> extends ItemFactory<I> {
    I simpleBake(Item.Properties properties);

    @Override
    default I bake(ItemEntryBuilder<I, ?> builder, Item.Properties properties) {
        return simpleBake(properties);
    }
}
