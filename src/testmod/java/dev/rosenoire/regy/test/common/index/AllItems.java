package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.common.content.datacomponent.ItemSkin;
import dev.rosenoire.regy.common.index.InternalDataComponents;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import static dev.rosenoire.regy.test.common.TestModCommon.REGY;
import static dev.rosenoire.regy.pipeline.content.ItemTransformers.*;

public interface AllItems {
    static void register() {
    }

    ItemEntry<Item> ELDRITCH_BLESSING = REGY
            .item("eldritch_blessing")
            .properties(properties -> properties.rarity(Rarity.UNCOMMON))
            .properties(single())
            .register();

    ItemEntry<Item> CLEAVER = REGY
            .item("cleaver")
            .properties(single())
            .swordTool(AllMaterials.AMARITE)
            .attackDamage(7)
            .attackSpeed(1.6f)
            .build()
            .skins("alternate", "femboy_beater", "sharpened", "butcher")
            .register();
}
