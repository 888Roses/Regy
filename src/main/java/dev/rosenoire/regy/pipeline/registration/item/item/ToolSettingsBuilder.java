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

    private boolean hasSetBlockingDisableTimeSeconds = false;

    public ToolSettingsBuilder(ItemEntryBuilder<I, P> owner) {
        this.owner = owner;
        material(ToolMaterial.IRON);
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
        this.hasSetBlockingDisableTimeSeconds = true;
        return this;
    }

    public ToolSettingsBuilder<I, P> tool(@NonNull UnaryOperator<ItemEntryBuilder<I, P>> toolFunction) {
        this.toolFunction = toolFunction;
        return this;
    }

    public ToolSettingsBuilder<I, P> pickaxe() {
        return tool(builder -> ItemMaps.pickaxe(builder, this.material));
    }

    public ToolSettingsBuilder<I, P> hoe() {
        return tool(builder -> ItemMaps.hoe(builder, this.material));
    }

    public ToolSettingsBuilder<I, P> shovel() {
        return tool(builder -> ItemMaps.shovel(builder, this.material));
    }

    public ToolSettingsBuilder<I, P> axe() {
        return tool(builder -> ItemMaps.pickaxe(builder, this.material))
                .map(builder -> builder.hasSetBlockingDisableTimeSeconds ? builder : builder.blockingDisableTime(5f));
    }

    public ToolSettingsBuilder<I, P> sword() {
        return tool(ItemMaps::sword);
    }

    public <B> B map(Function<ToolSettingsBuilder<I, P>, B> mapper) {
        return mapper.apply(this);
    }

    public ItemEntryBuilder<I, P> build() {
        return owner
                .map(builder -> this.toolFunction == null ? builder : this.toolFunction.apply(builder))
                .toolMaterial(material)
                .attackDamage(attackDamage)
                .attackSpeed(attackSpeed)
                .weaponComponent(durabilityDamage, blockingDisableTimeSeconds);
    }
}
