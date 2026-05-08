package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.pipeline.registration.item.group.VanillaCreativeTab;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemMaps;
import dev.rosenoire.regy.pipeline.registration.item.item.SpearProfiles;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import static dev.rosenoire.regy.test.common.TestModCommon.REGY;

public interface AllItems {
    ItemEntry<Item> SWORD_OF_DESPAIR = REGY
            .item("sword_of_despair")
            .attackDamage(7)
            .tab(VanillaCreativeTab.COMBAT)
            .register();

    ItemEntry<Item> FAKE_SPEAR = REGY
            .item("fake_spear")
            .material(AllMaterials.ELDEN)
            .spear()
            .properties()
            .copy(SpearProfiles.NETHERITE_SPEAR)
            .build()
            .build()
            .tag(ItemTags.SPEARS)
            .register();

    ItemEntry<Item> ELDRITCH_BLESSING = REGY
            .item("eldritch_blessing")
            .properties(properties -> properties.rarity(Rarity.UNCOMMON))
            .map(ItemMaps::singleStackSize)
            .register();

    ItemEntry<Item> ELDERN_PROSECUTOR = REGY
            .item("eldern_prosecutor")
            .map(ItemMaps::singleStackSize)
            .tool()
            .material(AllMaterials.ELDEN)
            .attackDamage(6f)
            .attackSpeed(1.8f)
            .sword()
            .build()
            .tag(ItemTags.SWORDS)
            .tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
            .handheldModel()
            .register();

    ItemEntry<AxeItem> ELDERN_VERDICT = REGY
            .axe("eldern_verdict", AxeItem::new)
            .map(ItemMaps::singleStackSize)
            .tool()
            .material(AllMaterials.ELDEN)
            .attackDamage(9.5F)
            .attackSpeed(1)
            .blockingDisableTime(5f)
            .axe()
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

    static void register() {
    }
}
