package dev.rosenoire.regy.pipeline.registration.item.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public record ItemDataState<I extends Item>(I item, ResourceKey<Item> resourceKey, String name) {
}
