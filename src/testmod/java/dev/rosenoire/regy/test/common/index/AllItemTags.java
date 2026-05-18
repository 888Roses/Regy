package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.pipeline.registration.tag.item.ItemTagEntry;
import net.minecraft.world.item.Items;

import static dev.rosenoire.regy.test.common.TestModCommon.REGY;

public interface AllItemTags {
    ItemTagEntry CUTE = REGY
            .itemTag("cute")
            .item(Items.AXOLOTL_BUCKET)
            .item(Items.AXOLOTL_SPAWN_EGG)
            .item(Items.HAPPY_GHAST_SPAWN_EGG)
            .register();

    static void register() {
    }
}
