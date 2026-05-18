package dev.rosenoire.regy.pipeline.registration.item.item;

import dev.rosenoire.regy.api.data.NonNullType;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.content.ItemTransformers;
import dev.rosenoire.regy.pipeline.factory.ItemFactory;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.block.BlockEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.group.CreativeTabEntry;
import dev.rosenoire.regy.pipeline.registration.item.group.CreativeTabGroup;
import dev.rosenoire.regy.pipeline.registration.item.group.VanillaCreativeTab;
import dev.rosenoire.regy.pipeline.registration.item.material.MaterialEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class ItemEntryBuilder<I extends Item, P> extends AbstractEntryBuilder<ItemEntry<I>, P> {
    protected final ItemFactory<I> itemFactory;
    protected final ResourceKey<Item> resourceKey;

    protected Item.Properties properties;
    protected ItemAttributeModifiers.Builder attributesBuilder = null;

    protected final CreativeTabGroup.Builder creativeTabBuilder = new CreativeTabGroup.Builder();
    protected @Nullable ToolMaterial toolMaterial;

    public ItemEntryBuilder(@NonNull AbstractRegy<?> owner, P parent, String identifier, ItemFactory<I> itemFactory) {
        super(owner, parent, identifier);
        this.itemFactory = itemFactory;
        this.resourceKey = ResourceKey.create(Registries.ITEM, identifier());
        this.properties = new Item.Properties();
    }

    @Override
    public @NonNull ItemEntry<I> register() {
        regy().log.info("Starting registration for item '{}'...", identifier());

        if (this.attributesBuilder != null) {
            regy().log.info("|---- Building and assigning attribute builder...");
            if (this.properties instanceof ItemPropertiesExtension extension) {
                this.properties = extension.forceSetAttributes(this.attributesBuilder.build());
            }
        }

        regy().log.info("|---- Setting property ID and registering...");
        this.properties = this.properties.setId(resourceKey);
        var item = itemFactory.bake(this, this.properties);
        Registry.register(BuiltInRegistries.ITEM, resourceKey, item);

        regy().log.info("|---- Creating Item Entry...");
        var itemEntry = regy().entry(new ItemEntry<>(
                item,
                this.resourceKey,
                this.material(),
                this.creativeTabBuilder.build()
        ));

        regy().log.info("|-- Finished registration successfully!");
        return itemEntry;
    }

    @Override
    protected Identifier regyIdentifier() {
        return getRegyIdentifierFromRegistry(this.resourceKey);
    }

    @Override
    public P build() {
        if (this.getParent() instanceof BlockEntryBuilder<?, ?>) return getParent();
        return super.build();
    }

    // region accessors

    public ResourceKey<Item> resourceKey() {
        return this.resourceKey;
    }

    public CreativeTabGroup.Builder creativeTabs() {
        return creativeTabBuilder;
    }

    public @Nullable ToolMaterial material() {
        return toolMaterial;
    }

    // endregion

    // region modifiers

    public <B> B transform(Function<ItemEntryBuilder<I, P>, B> mapper) {
        return mapper.apply(this);
    }

    public ItemEntryBuilder<I, P> properties(Item.@NonNull Properties properties) {
        this.properties = properties;
        return this;
    }

    public ItemEntryBuilder<I, P> properties(@NonNull UnaryOperator<Item.@NonNull Properties> map) {
        return properties(map.apply(this.properties));
    }

    public ItemEntryBuilder<I, P> tab(@Nullable CreativeModeTab creativeTab) {
        this.creativeTabBuilder.addTab(creativeTab);
        return this;
    }

    public ItemEntryBuilder<I, P> tab(@Nullable CreativeTabEntry entry) {
        return Optional.ofNullable(entry)
                .map(CreativeTabEntry::get)
                .map(this::tab)
                .orElse(this);
    }

    public ItemEntryBuilder<I, P> tab(@Nullable VanillaCreativeTab vanillaTab) {
        this.creativeTabBuilder.addTab(vanillaTab);
        return this;
    }

    public ItemEntryBuilder<I, P> tab(@Nullable ResourceKey<CreativeModeTab> resourceKey) {
        this.creativeTabBuilder.addTab(resourceKey);
        return this;
    }

    public ItemEntryBuilder<I, P> hideFromMainTab() {
        this.creativeTabBuilder.showInMainTab(false);
        return this;
    }

    public ItemEntryBuilder<I, P> showInMainTab() {
        this.creativeTabBuilder.showInMainTab(true);
        return this;
    }

    public ItemEntryBuilder<I, P> attribute(ItemAttributeModifiers.@NonNull Entry entry) {
        if (this.attributesBuilder == null) {
            this.attributesBuilder = ItemAttributeModifiers.builder();
        }

        this.attributesBuilder.add(entry.attribute(), entry.modifier(), entry.slot(), entry.display());
        return this;
    }

    public ItemEntryBuilder<I, P> attribute(@NonNull Holder<@NonNull Attribute> holder, @NonNull AttributeModifier attributeModifier, @NonNull EquipmentSlotGroup equipmentSlotGroup) {
        return this.attribute(new ItemAttributeModifiers.Entry(holder, attributeModifier, equipmentSlotGroup));
    }

    public ItemEntryBuilder<I, P> attribute(@NonNull Holder<@NonNull Attribute> holder, @NonNull AttributeModifier attributeModifier, @NonNull EquipmentSlotGroup equipmentSlotGroup, ItemAttributeModifiers.@NonNull Display display) {
        return this.attribute(new ItemAttributeModifiers.Entry(holder, attributeModifier, equipmentSlotGroup, display));
    }

    public ItemEntryBuilder<I, P> material(@NonNull MaterialEntry entry) {
        return this.material(entry.get());
    }

    public ItemEntryBuilder<I, P> material(@NonNull ToolMaterial material) {
        return this.material(material, false);
    }

    public ItemEntryBuilder<I, P> material(@NonNull ToolMaterial material, boolean discrete) {
        this.toolMaterial = material;
        return this.properties(properties -> discrete ? properties : properties
                                                                     .durability(material.durability())
                                                                     .repairable(material.repairItems())
                                                                     .enchantable(material.enchantmentValue()));
    }

    public ItemEntryBuilder<I, P> noMaterial() {
        this.toolMaterial = null;
        return this;
    }

    public ItemEntryBuilder<I, P> attackDamage(float attackDamage) {
        var effectiveAttackDamage = attackDamage - 1f;
        var attributeModifier = new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, effectiveAttackDamage, AttributeModifier.Operation.ADD_VALUE);
        return this.attribute(Attributes.ATTACK_DAMAGE, attributeModifier, EquipmentSlotGroup.MAINHAND);
    }

    public ItemEntryBuilder<I, P> attackSpeed(float attackSpeed) {
        var effectiveAttackSpeed = attackSpeed - 4f;
        var attributeModifier = new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, effectiveAttackSpeed, AttributeModifier.Operation.ADD_VALUE);
        return this.attribute(Attributes.ATTACK_SPEED, attributeModifier, EquipmentSlotGroup.MAINHAND);
    }

    public <@NonNullType T> ItemEntryBuilder<I, P> component(@NonNull DataComponentType<T> dataComponentType, @NonNull T object) {
        return properties(properties -> properties.component(dataComponentType, object));
    }

    public <@NonNullType T> ItemEntryBuilder<I, P> removeComponent(@NonNull DataComponentType<T> dataComponentType) {
        //noinspection DataFlowIssue
        return properties(properties -> properties.component(dataComponentType, null));
    }

    public SpearSettingsBuilder<I, P> spear() {
        return new SpearSettingsBuilder<>(this);
    }

    public ItemEntryBuilder<I, P> tool(float defaultMiningSpeed, int damagePerBlock, boolean canDestroyBlocksInCreativeMode, ToolRuleCollector collector) {
        return transform(builder -> {
            HolderGetter<Block> blockRegistry = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
            var toolRules = new ArrayList<Tool.Rule>();
            collector.collect(blockRegistry, toolRules::add);
            return builder.component(DataComponents.TOOL, new Tool(toolRules, defaultMiningSpeed, damagePerBlock, canDestroyBlocksInCreativeMode));
        });
    }

    public ToolSettingsBuilder<I, P> tool(ToolMaterial material, @NonNull UnaryOperator<ItemEntryBuilder<I, P>> toolFunction) {
        return new ToolSettingsBuilder<>(this, material).toolFunction(toolFunction);
    }

    public ToolSettingsBuilder<I, P> tool(MaterialEntry material, @NonNull UnaryOperator<ItemEntryBuilder<I, P>> toolFunction) {
        return tool(material.get(), toolFunction);
    }

    public ToolSettingsBuilder<I, P> pickaxeTool(ToolMaterial material) {
        return tool(material, builder -> ItemTransformers.pickaxe(builder, material));
    }

    public ToolSettingsBuilder<I, P> pickaxeTool(MaterialEntry material) {
        return hoeTool(material.get());
    }

    public ToolSettingsBuilder<I, P> hoeTool(ToolMaterial material) {
        return tool(material, builder -> ItemTransformers.hoe(builder, material));
    }

    public ToolSettingsBuilder<I, P> hoeTool(MaterialEntry material) {
        return hoeTool(material.get());
    }

    public ToolSettingsBuilder<I, P> shovelTool(ToolMaterial material) {
        return tool(material, builder -> ItemTransformers.shovel(builder, material));
    }

    public ToolSettingsBuilder<I, P> shovelTool(MaterialEntry material) {
        return shovelTool(material.get());
    }

    public ToolSettingsBuilder<I, P> axeTool(ToolMaterial material) {
        return tool(material, builder -> ItemTransformers.pickaxe(builder, material))
                .map(builder -> builder.blockingDisableTime(5f));
    }

    public ToolSettingsBuilder<I, P> axeTool(MaterialEntry material) {
        return axeTool(material.get());
    }

    public ToolSettingsBuilder<I, P> swordTool(ToolMaterial material) {
        return tool(material, ItemTransformers::sword);
    }

    public ToolSettingsBuilder<I, P> swordTool(MaterialEntry material) {
        return swordTool(material.get());
    }

    public ItemEntryBuilder<I, P> weaponComponent(int itemDamagePerAttack, float disableBlockForSeconds) {
        return component(DataComponents.WEAPON, new Weapon(itemDamagePerAttack, disableBlockForSeconds));
    }

    public ItemEntryBuilder<I, P> weaponComponent(int itemDamagePerAttack) {
        return weaponComponent(itemDamagePerAttack, 0F);
    }

    public ItemEntryBuilder<I, P> weaponComponent() {
        return weaponComponent(1);
    }

    // endregion
}

