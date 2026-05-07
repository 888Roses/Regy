package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemMaps;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;

import static dev.rosenoire.regy.test.common.TestModCommon.REGY;

public interface AllItems {
    ItemEntry<Item> ELDRITCH_BLESSING = REGY
            .item("eldritch_blessing")
            .properties(properties -> properties.rarity(Rarity.UNCOMMON))
            .map(ItemMaps::singleStackSize)
            .register();

    ItemEntry<Item> ELDERN_PROSECUTOR = REGY
            .item("eldern_prosecutor")
            .map(ItemMaps::singleStackSize)
            .swordSettings()
            .material(ToolMaterial.NETHERITE)
            .attackDamage(6f)
            .attackSpeed(1.8f)
            .build()
            .handheldModel()
            .register();

    ItemEntry<Item> ELDERN_VERDICT = REGY
            .item("eldern_verdict")
            .map(ItemMaps::singleStackSize)
            .swordSettings()
            .material(ToolMaterial.NETHERITE)
            .attackDamage(9.5F)
            .attackSpeed(1)
            .blockingDisableTime(5f)
            .build()
            .tag(ItemTags.SWORDS)
            .tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
            .handheldModel()
            .register();

    ItemEntry<Item> ELDEN_INGOT = REGY
            .item("elden_ingot")
            // .recipe(AllRecipes::eldenNuggetsToEldenIngot)
            .register();

    ItemEntry<Item> ELDEN_NUGGET = REGY
            .item("elden_nugget")
            // .recipe(AllRecipes::eldenIngotToEldenNuggets)
            .register();

    static void register() {
    }
}
