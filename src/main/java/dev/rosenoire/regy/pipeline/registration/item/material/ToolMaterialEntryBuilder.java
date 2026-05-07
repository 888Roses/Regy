package dev.rosenoire.regy.pipeline.registration.item.material;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

@SuppressWarnings({"UnusedReturnValue", "ConstantValue", "unused"})
public class ToolMaterialEntryBuilder<P> extends AbstractEntryBuilder<MaterialEntry, P> {
    private @NonNull TagKey<Block> incorrectBlocksForDrops;
    private @NonNull TagKey<Item> repairItems;
    private int durability;
    private float speed;
    private float attackDamageBonus;
    private int enchantmentValue;

    public ToolMaterialEntryBuilder(@NonNull AbstractRegy<?> owner, P parent, String identifier) {
        super(owner, parent, identifier);

        this.incorrectBlocksForDrops(BlockTags.INCORRECT_FOR_IRON_TOOL);
        this.repairItems(ItemTags.REPAIRS_IRON_ARMOR);
    }

    @Override
    public @NonNull MaterialEntry register() {
        if (incorrectBlocksForDrops == null) {
            throw new NullPointerException("incorrectBlocksForDrops tag cannot be null for material entry builder with id '" + identifier() + "'!");
        }

        if (repairItems == null) {
            throw new NullPointerException("repairItem tag cannot be null for material entry builder with id '" + identifier() + "'!");
        }

        var material = new ToolMaterial(incorrectBlocksForDrops, durability, speed, attackDamageBonus, enchantmentValue, repairItems);
        return getRegy().entry(new MaterialEntry(material));
    }

    // region modifiers

    public ToolMaterialEntryBuilder<P> copy(@NonNull ToolMaterial other) {
        return this.incorrectBlocksForDrops(other).repairItems(other).durability(other).speed(other).attackDamageBonus(other).enchantmentValue(other);
    }

    public ToolMaterialEntryBuilder<P> incorrectBlocksForDrops(@NonNull TagKey<Block> tag) {
        this.incorrectBlocksForDrops = tag;
        return this;
    }

    public ToolMaterialEntryBuilder<P> repairItems(@NonNull TagKey<Item> tag) {
        this.repairItems = tag;
        return this;
    }

    public ToolMaterialEntryBuilder<P> durability(int durability) {
        this.durability = durability;
        return this;
    }

    public ToolMaterialEntryBuilder<P> speed(float speed) {
        this.speed = speed;
        return this;
    }

    public ToolMaterialEntryBuilder<P> attackDamageBonus(float attackDamageBonus) {
        this.attackDamageBonus = attackDamageBonus;
        return this;
    }

    public ToolMaterialEntryBuilder<P> enchantmentValue(int enchantmentValue) {
        this.enchantmentValue = enchantmentValue;
        return this;
    }

    public ToolMaterialEntryBuilder<P> incorrectBlocksForDrops(@NonNull ToolMaterial other) {
        return this.incorrectBlocksForDrops(other.incorrectBlocksForDrops());
    }

    public ToolMaterialEntryBuilder<P> repairItems(@NonNull ToolMaterial other) {
        return this.repairItems(other.repairItems());
    }

    public ToolMaterialEntryBuilder<P> durability(@NonNull ToolMaterial other) {
        return this.durability(other.durability());
    }

    public ToolMaterialEntryBuilder<P> speed(@NonNull ToolMaterial other) {
        return this.speed(other.speed());
    }

    public ToolMaterialEntryBuilder<P> attackDamageBonus(@NonNull ToolMaterial other) {
        return this.attackDamageBonus(other.attackDamageBonus());
    }

    public ToolMaterialEntryBuilder<P> enchantmentValue(@NonNull ToolMaterial other) {
        return this.enchantmentValue(other.enchantmentValue());
    }

    // endregion
}
