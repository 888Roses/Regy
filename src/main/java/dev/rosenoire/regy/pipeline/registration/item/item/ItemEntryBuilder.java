package dev.rosenoire.regy.pipeline.registration.item.item;

import dev.rosenoire.regy.api.data.RegistryUtils;
import dev.rosenoire.regy.api.model.ModelUtils;
import dev.rosenoire.regy.api.text.NamingConventions;
import dev.rosenoire.regy.common.RegyCommon;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.datagen.DataGenObject;
import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.*;
import dev.rosenoire.regy.pipeline.factory.ItemFactory;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.group.CreativeTabEntry;
import dev.rosenoire.regy.pipeline.registration.item.material.MaterialEntry;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class ItemEntryBuilder<I extends Item, P> extends AbstractEntryBuilder<ItemEntry<I>, P> implements DataGenObject {
    private final ItemFactory<I> itemFactory;
    private final ResourceKey<Item> resourceKey;

    private Item.Properties properties;
    private ItemAttributeModifiers.Builder attributesBuilder = null;

    private @Nullable CreativeTabEntry creativeTab;
    private @Nullable ToolMaterial toolMaterial;

    public ItemEntryBuilder(@NonNull AbstractRegy<?> owner, P parent, String identifier, ItemFactory<I> itemFactory) {
        super(owner, parent, identifier);
        this.itemFactory = itemFactory;
        this.resourceKey = ResourceKey.create(Registries.ITEM, identifier());
        this.properties = new Item.Properties().setId(resourceKey);

        this.simpleName();
        this.simpleModel();
    }

    @Override
    public @NonNull ItemEntry<I> register() {
        RegyCommon.log.info("Starting registration for entry builder: '{}'...", identifier());

        if (this.attributesBuilder != null) {
            RegyCommon.log.info("  Building attribute building...");
            if (this.properties instanceof ItemPropertiesExtension extension) {
                this.properties = extension.forceSetAttributes(this.attributesBuilder.build());
            }
        }

        var item = itemFactory.bake(this, this.properties);
        Registry.register(BuiltInRegistries.ITEM, resourceKey, item);

        itemDataState = new ItemDataState<>(item, this.resourceKey, this.path());
        RegyCommon.log.info("  Adding data-gen data...");
        getRegy().dataGeneration().addData(this);

        var entry = new ItemEntry<>(item, resourceKey, material());
        RegyCommon.log.info("  Adding entry...");
        entry = getRegy().entry(entry);
        RegyCommon.log.info("Finished registration for entry builder: '{}'...", identifier());
        return entry;
    }

    // region accessors

    public ResourceKey<Item> resourceKey() {
        return this.resourceKey;
    }

    public @Nullable CreativeTabEntry creativeTab() {
        return creativeTab;
    }

    public @Nullable ToolMaterial material() {
        return toolMaterial;
    }

    // endregion

    // region datagen

    /// `String` representing the name to generate for this item entry. Can be null. If
    /// null, no name should be generated for this item.
    private @Nullable String generatedName;
    /// [ModelInstruction] of [I] representing the model to generate for that item. Can
    /// be null. If null, the model generation process should be skipped entirely.
    private @Nullable ModelInstruction<I> modelInstruction = ModelInstruction.simple();
    /// [List] of [TagKey] of [Item] representing every tag the built item entry should
    /// be made part of when running data-gen.
    private final List<TagKey<Item>> tagStorage = new ArrayList<>();
    /// [List] of [Recipe] of [I] representing every recipe related to this item entry.
    private final List<Recipe<I>> recipeStorage = new ArrayList<>();

    /// @apiNote Should not be used raw! This variable is ALWAYS right up until
    ///                                                 [#register()] is called. Then, all the data-gen related methods are
    ///         called,
    ///                 for
    ///                                                 which this data state system was created, and then it is discarded.
    ///         For that
    ///                                                 reason, it should NEVER be used as a source of information.
    private ItemDataState<I> itemDataState;

    /// Represents the translation key for this item entry generated using its
    /// [#resourceKey()].
    protected String descriptionId() {
        return RegistryUtils.toDescriptionId(this.resourceKey());
    }

    @Override
    public void collectDataGenProviders(DataGenProviderConsumer collector) {
        collector.addProvider(this::dataGenLangProvider);
        collector.addProvider(this::dataGenModelProvider);
        collector.addProvider(this::dataGenItemTagProvider);
        collector.addProvider(this::dataGenRecipeProvider);
    }

    private void dataGenRecipeProvider(DataGeneration gen) {
        gen.<RecipeDataGenerator>getGeneratorOptional(DataGenerators.RECIPES).ifPresent(recipes ->
                this.recipeStorage.forEach(instruction ->
                        recipes.registerRecipe((provider, output) ->
                                instruction.generateRecipe(gen, provider, output, this.itemDataState))
                )
        );
    }

    private void dataGenItemTagProvider(DataGeneration gen) {
        gen.<ItemTagDataGenerator>getGeneratorOptional(DataGenerators.ITEM_TAGS).ifPresent(tags -> {
            synchronized (this.tagStorage) {
                this.tagStorage.forEach(tag -> tags.tag(
                        tag,
                        builder -> builder.add(itemDataState.item()).setReplace(false)
                ));
            }
        });
    }

    private void dataGenModelProvider(DataGeneration gen) {
        if (this.modelInstruction == null) {
            return;
        }

        gen.<ModelDataGenerator>getGeneratorOptional(DataGenerators.MODELS).ifPresent(models ->
                models.addItemModel(() -> itemModelGenerator ->
                        modelInstruction.generateModel(gen, itemModelGenerator, itemDataState)
                )
        );
    }

    private void dataGenLangProvider(DataGeneration gen) {
        gen.<LangDataGenerator>getGeneratorOptional(DataGenerators.LANG).ifPresent(lang ->
                lang.add(descriptionId(), this.generatedName)
        );
    }

    @FunctionalInterface
    public interface ModelInstruction<I extends Item> {
        void generateModel(DataGeneration dataGen, ItemModelGenerators generators, ItemDataState<I> data);

        static <I extends Item> ModelInstruction<I> simple() {
            return (dataGen, generators, data) ->
                    generators.generateFlatItem(data.item(), ModelTemplates.FLAT_ITEM);
        }

        static <I extends Item> ModelInstruction<I> handheld(ItemModel.Unbaked gui, ItemModel.Unbaked handheld) {
            return (dataGen, generators, data) ->
                    generators.itemModelOutput.accept(data.item(), ItemModelGenerators.createFlatModelDispatch(gui, handheld));
        }
    }

    @FunctionalInterface
    public interface Recipe<I extends Item> {
        void generateRecipe(DataGeneration dataGeneration, RecipeProvider provider, RecipeOutput output, ItemDataState<I> data);
    }

    // endregion

    // region modifiers

    public <B> B map(Function<ItemEntryBuilder<I, P>, B> mapper) {
        return mapper.apply(this);
    }

    public ItemEntryBuilder<I, P> name(@NotNull String name) {
        this.generatedName = name;
        return this;
    }

    public ItemEntryBuilder<I, P> simpleName() {
        return name(NamingConventions.HUMAN_TEXT.transform(path()));
    }

    public ItemEntryBuilder<I, P> customName() {
        this.generatedName = null;
        return this;
    }

    public ItemEntryBuilder<I, P> properties(@NotNull Item.Properties properties) {
        this.properties = properties;
        return this;
    }

    public ItemEntryBuilder<I, P> properties(UnaryOperator<Item.Properties> mod) {
        return properties(mod.apply(this.properties));
    }

    public ItemEntryBuilder<I, P> tab(@NonNull CreativeTabEntry creativeTab) {
        this.creativeTab = creativeTab;
        return this;
    }

    public ItemEntryBuilder<I, P> customModel() {
        this.modelInstruction = null;
        return this;
    }

    public ItemEntryBuilder<I, P> model(@NonNull ModelInstruction<I> instruction) {
        this.modelInstruction = instruction;
        return this;
    }

    public ItemEntryBuilder<I, P> simpleModel() {
        return model(ModelInstruction.simple());
    }

    public ItemEntryBuilder<I, P> handheldModel(ItemModel.Unbaked gui, ItemModel.Unbaked handheld) {
        return model(ModelInstruction.handheld(gui, handheld));
    }

    public ItemEntryBuilder<I, P> handheldModel(String guiSuffix, String handheldSuffix) {
        return this.handheldModel(
                ItemModelUtils.plainModel(ModelUtils.getItemSubModelId(identifier(), guiSuffix)),
                ItemModelUtils.plainModel(ModelUtils.getItemSubModelId(identifier(), handheldSuffix))
        );
    }

    public ItemEntryBuilder<I, P> handheldModel() {
        return handheldModel("", "handheld");
    }

    public ItemEntryBuilder<I, P> attribute(ItemAttributeModifiers.Entry entry) {
        if (this.attributesBuilder == null) {
            this.attributesBuilder = ItemAttributeModifiers.builder();
        }

        this.attributesBuilder.add(entry.attribute(), entry.modifier(), entry.slot(), entry.display());
        return this;
    }

    public ItemEntryBuilder<I, P> attribute(Holder<Attribute> holder, AttributeModifier attributeModifier, EquipmentSlotGroup equipmentSlotGroup) {
        return this.attribute(new ItemAttributeModifiers.Entry(holder, attributeModifier, equipmentSlotGroup));
    }

    public ItemEntryBuilder<I, P> attribute(Holder<Attribute> holder, AttributeModifier attributeModifier, EquipmentSlotGroup equipmentSlotGroup, ItemAttributeModifiers.Display display) {
        return this.attribute(new ItemAttributeModifiers.Entry(holder, attributeModifier, equipmentSlotGroup, display));
    }

    public ItemEntryBuilder<I, P> material(MaterialEntry materialEntry) {
        return this.material(materialEntry.get());
    }

    public ItemEntryBuilder<I, P> material(ToolMaterial material) {
        this.toolMaterial = material;

        if (material != null) {
            return this.properties(properties -> properties
                    .durability(material.durability())
                    .repairable(material.repairItems())
                    .enchantable(material.enchantmentValue()));
        }

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

    public <T> ItemEntryBuilder<I, P> component(DataComponentType<T> dataComponentType, T object) {
        return properties(properties -> properties.component(dataComponentType, object));
    }

    public <T> ItemEntryBuilder<I, P> removeComponent(DataComponentType<T> dataComponentType) {
        return properties(properties -> properties.component(dataComponentType, null));
    }

    public ToolSettingsBuilder<I, P> tool() {
        return new ToolSettingsBuilder<>(this);
    }

    public SpearSettingsBuilder<I, P> spear() {
        return new SpearSettingsBuilder<>(this);
    }

    public ItemEntryBuilder<I, P> tool(Function<HolderGetter<Block>, Tool> func) {
        return map(builder -> {
            HolderGetter<Block> blockRegistry = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
            return builder.component(DataComponents.TOOL, func.apply(blockRegistry));
        });
    }

    @FunctionalInterface
    public interface ToolRuleCollector {
        void collect(HolderGetter<Block> lookup, Consumer<Tool.Rule> collector);
    }

    public ItemEntryBuilder<I, P> tool(float defaultMiningSpeed, int damagePerBlock, boolean canDestroyBlocksInCreativeMode, ToolRuleCollector collector) {
        return map(builder -> {
            HolderGetter<Block> blockRegistry = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
            var toolRules = new ArrayList<Tool.Rule>();
            collector.collect(blockRegistry, toolRules::add);
            return builder.component(DataComponents.TOOL, new Tool(toolRules, defaultMiningSpeed, damagePerBlock, canDestroyBlocksInCreativeMode));
        });
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

    public ItemEntryBuilder<I, P> tag(TagKey<Item> key) {
        tagStorage.add(key);
        return this;
    }

    public ItemEntryBuilder<I, P> recipe(Recipe<I> recipe) {
        this.recipeStorage.add(recipe);
        return this;
    }

    // endregion
}

