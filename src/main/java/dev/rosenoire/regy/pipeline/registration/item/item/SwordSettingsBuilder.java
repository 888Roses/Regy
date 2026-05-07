package dev.rosenoire.regy.pipeline.registration.item.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import org.jetbrains.annotations.Nullable;

public class SwordSettingsBuilder<I extends Item, P> {
    private final ItemEntryBuilder<I, P> owner;
    private @Nullable ToolMaterial material;
    private float attackDamage = 5f;
    private float attackSpeed = 1.6f;
    private int durabilityDamage = 1;
    private float blockingDisableTimeSeconds = 0f;

    public SwordSettingsBuilder(ItemEntryBuilder<I, P> owner) {
        this.owner = owner;
    }

    public SwordSettingsBuilder<I, P> material(ToolMaterial toolMaterial) {
        this.material = toolMaterial;
        return this;
    }

    public SwordSettingsBuilder<I, P> attackDamage(float attackDamage) {
        this.attackDamage = attackDamage;
        return this;
    }

    public SwordSettingsBuilder<I, P> attackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
        return this;
    }

    public SwordSettingsBuilder<I, P> durabilityDamage(int durabilityDamage) {
        this.durabilityDamage = durabilityDamage;
        return this;
    }

    public SwordSettingsBuilder<I, P> blockingDisableTime(float blockingDisableTimeSeconds) {
        this.blockingDisableTimeSeconds = blockingDisableTimeSeconds;
        return this;
    }

    public ItemEntryBuilder<I, P> build() {
        return owner
                .map(ItemMaps::swordTool)
                .toolMaterial(material)
                .attackDamage(attackDamage)
                .attackSpeed(attackSpeed)
                .weaponComponent(durabilityDamage, blockingDisableTimeSeconds);
    }
}
