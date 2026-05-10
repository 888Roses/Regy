package dev.rosenoire.regy.pipeline.registration.item.item;

import dev.rosenoire.regy.pipeline.registration.item.material.MaterialEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ToolSettingsBuilder<I extends Item, P> {
    private final ItemEntryBuilder<I, P> owner;
    private @NonNull ToolMaterial material;
    private float attackDamage = 5f;
    private float attackSpeed = 1.6f;
    private int durabilityDamage = 1;
    private float blockingDisableTimeSeconds = 0f;
    private @Nullable UnaryOperator<ItemEntryBuilder<I, P>> toolFunction;

    public ToolSettingsBuilder(ItemEntryBuilder<I, P> owner, @NonNull ToolMaterial material) {
        this.owner = owner;
        this.material = material;
    }

    public ToolSettingsBuilder<I, P> material(@NonNull ToolMaterial toolMaterial) {
        this.material = toolMaterial;
        return this;
    }

    public ToolSettingsBuilder<I, P> material(MaterialEntry material) {
        return this.material(material.get());
    }

    public ToolSettingsBuilder<I, P> attackDamage(float attackDamage) {
        this.attackDamage = attackDamage;
        return this;
    }

    public ToolSettingsBuilder<I, P> attackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
        return this;
    }

    public ToolSettingsBuilder<I, P> durabilityDamage(int durabilityDamage) {
        this.durabilityDamage = durabilityDamage;
        return this;
    }

    public ToolSettingsBuilder<I, P> blockingDisableTime(float blockingDisableTimeSeconds) {
        this.blockingDisableTimeSeconds = blockingDisableTimeSeconds;
        return this;
    }

    public ToolSettingsBuilder<I, P> toolFunction(@NonNull UnaryOperator<ItemEntryBuilder<I, P>> toolFunction) {
        this.toolFunction = toolFunction;
        return this;
    }

    public <B> B map(Function<ToolSettingsBuilder<I, P>, B> mapper) {
        return mapper.apply(this);
    }

    public ItemEntryBuilder<I, P> build() {
        return owner
                .transform(builder -> this.toolFunction == null ? builder : this.toolFunction.apply(builder))
                .material(material)
                .attackDamage(attackDamage)
                .attackSpeed(attackSpeed)
                .weaponComponent(durabilityDamage, blockingDisableTimeSeconds);
    }
}
