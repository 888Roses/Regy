package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import static dev.rosenoire.regy.test.common.TestModCommon.REGY;
import static dev.rosenoire.regy.pipeline.content.ItemTransformers.*;

public interface AllItems {
    ItemEntry<Item> ELDRITCH_BLESSING = REGY
            .item("eldritch_blessing")
            .properties(properties -> properties.rarity(Rarity.UNCOMMON))
            .properties(single())
            .register();

    ItemEntry<Item> ELDERN_PROSECUTOR = REGY
            .item("eldern_prosecutor")
            .properties(single())
            .swordTool(AllMaterials.ELDEN)
            .attackDamage(6f)
            .attackSpeed(1.8f)
            .build()
            .tag(ItemTags.SWORDS)
            .tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
            .handheldModel()
            .register();

    ItemEntry<AxeItem> ELDERN_VERDICT = REGY
            .axe("eldern_verdict", AxeItem::new)
            .properties(single())
            .axeTool(AllMaterials.ELDEN)
            .attackDamage(9.5F)
            .attackSpeed(1)
            .blockingDisableTime(5f)
            .build()
            .tag(ItemTags.AXES)
            .tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
            .handheldModel()
            .register();

    ItemEntry<Item> ELDEN_INGOT = REGY
            .item("elden_ingot")
            .recipe(AllRecipes::eldenNuggetsToEldenIngot)
            .register();

    ItemEntry<Item> ELDEN_NUGGET = REGY
            .item("elden_nugget")
            .recipe(AllRecipes::eldenIngotToEldenNuggets)
            .register();

    ItemEntry<Item> BURNING_CURSE = REGY
            .item("burning_curse")
            .properties(single())
            .register();

    ItemEntry<Item> ELDEN_BASE = REGY
            .item("elden_base")
            .properties(single())
            .register();

    ItemEntry<Item> ELDEN_RING = REGY
            .item("elden_ring")
            .properties(single())
            .register();

    ItemEntry<Item> ELDEN_FRACTURE = REGY
            .item("elden_fracture")
            .properties(single())
            .register();

    static void register() {
    }
}
