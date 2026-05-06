package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.registration.ItemEntry;
import dev.rosenoire.regy.pipeline.registration.ItemEntryBuilder;
import dev.rosenoire.regy.test.common.TestModCommon;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import static dev.rosenoire.regy.test.common.TestModCommon.REGY;

public interface AllItems {
    ItemEntry<Item> ELDRITCH_BLESSING = REGY
            .item("eldritch_blessing", Item::new)
            .properties(properties -> properties.rarity(Rarity.UNCOMMON))
            .transform(AllItems::singleStackSize)
            .register();

    static void register() {
    }

    private static <I extends Item, R extends AbstractRegy<?>> ItemEntryBuilder<I, R> singleStackSize(ItemEntryBuilder<I, R> builder) {
        return builder.properties(properties -> properties.stacksTo(1));
    }
}
