package dev.rosenoire.regy.pipeline.registration.item.group;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

public enum VanillaCreativeTab {
    BUILDING_BLOCKS(CreativeModeTabs.BUILDING_BLOCKS),
    COLORED_BLOCKS(CreativeModeTabs.COLORED_BLOCKS),
    NATURAL_BLOCKS(CreativeModeTabs.NATURAL_BLOCKS),
    FUNCTIONAL_BLOCKS(CreativeModeTabs.FUNCTIONAL_BLOCKS),
    REDSTONE_BLOCKS(CreativeModeTabs.REDSTONE_BLOCKS),
    HOTBAR(CreativeModeTabs.HOTBAR),
    SEARCH(CreativeModeTabs.SEARCH),
    TOOLS_AND_UTILITIES(CreativeModeTabs.TOOLS_AND_UTILITIES),
    COMBAT(CreativeModeTabs.COMBAT),
    FOOD_AND_DRINKS(CreativeModeTabs.FOOD_AND_DRINKS),
    INGREDIENTS(CreativeModeTabs.INGREDIENTS),
    SPAWN_EGGS(CreativeModeTabs.SPAWN_EGGS),
    OP_BLOCKS(CreativeModeTabs.OP_BLOCKS),
    INVENTORY(CreativeModeTabs.INVENTORY),
    ;

    private final ResourceKey<CreativeModeTab> resourceKey;

    VanillaCreativeTab(ResourceKey<CreativeModeTab> resourceKey) {
        this.resourceKey = resourceKey;
    }

    public ResourceKey<CreativeModeTab> resourceKey() {
        return resourceKey;
    }
}
