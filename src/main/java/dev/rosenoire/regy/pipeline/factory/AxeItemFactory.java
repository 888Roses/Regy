package dev.rosenoire.regy.pipeline.factory;

import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntryBuilder;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

@FunctionalInterface
public interface AxeItemFactory<A extends AxeItem> extends ItemFactory<A> {
    ToolMaterial DEFAULT_MATERIAL = ToolMaterial.IRON;
    float DEFAULT_ATTACK_DAMAGE = 12;
    float DEFAULT_ATTACK_SPEED = 0.9f - 4f;

    A bakeAxe(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties properties);

    @Override
    default A bake(ItemEntryBuilder<A, ?> builder, Item.Properties properties) {
        return bakeAxe(DEFAULT_MATERIAL, DEFAULT_ATTACK_DAMAGE, DEFAULT_ATTACK_SPEED, properties);
    }
}
