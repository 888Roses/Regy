package dev.rosenoire.regy.pipeline.registration.item.item;

import dev.rosenoire.regy.api.data.NonNullType;
import dev.rosenoire.regy.api.event.ValueEvent;
import dev.rosenoire.regy.api.model.ModelUtils;
import dev.rosenoire.regy.api.text.NamingConventions;
import dev.rosenoire.regy.common.RegyCommon;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.content.ItemTransformers;
import dev.rosenoire.regy.pipeline.datagen.DataGenObject;
import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.*;
import dev.rosenoire.regy.pipeline.factory.ItemFactory;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.block.BlockEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.group.CreativeTabEntry;
import dev.rosenoire.regy.pipeline.registration.item.group.CreativeTabGroup;
import dev.rosenoire.regy.pipeline.registration.item.group.VanillaCreativeTab;
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
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
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
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class ItemEntryBuilder<I extends Item, P> extends AbstractEntryBuilder<ItemEntry<I>, P> implements DataGenObject {
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

        this.properties = this.properties.setId(resourceKey);
        var item = itemFactory.bake(this, this.properties);
        Registry.register(BuiltInRegistries.ITEM, resourceKey, item);

        itemDataState = new ItemDataState<>(item, this.resourceKey, this.path());
        RegyCommon.log.info("  Adding data-gen data...");
        // TODO: Automatize!
        getRegy().dataGeneration().addData(this);

        var entry = new ItemEntry<>(item, resourceKey, material(), creativeTabBuilder.build(), this.tagStorage);
        RegyCommon.log.info("  Adding entry...");
        entry = getRegy().entry(entry);
        RegyCommon.log.info("Finished registration for entry builder: '{}'...", identifier());
        return entry;
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

    // region datagen

    /// `String` representing the name to generate for this item entry. Can be null. If
    /// null, no name should be generated for this item.
    protected @Nullable String generatedName;
    /// [ModelInstruction] of [I] representing the model to generate for that item. Can
    /// be null. If null, the model generation process should be skipped entirely.
    protected @Nullable ModelInstruction<I> modelInstruction = ModelInstruction.simple();
    /// [List] of [TagKey] of [Item] representing every tag the built item entry should
    /// be made part of when running data-gen.
    protected final List<TagKey<Item>> tagStorage = new ArrayList<>();
    /// [List] of [Recipe] of [I] representing every recipe related to this item entry.
    protected final List<Recipe<I>> recipeStorage = new ArrayList<>();
    protected final ValueEvent<@NonNull DataGenProviderConsumer> onCollectProviders = new ValueEvent<>();

    /// @apiNote Should not be used raw! This variable is ALWAYS right up until
    /// [#register()] is called. Then, all the data-gen related methods are called, for
    /// which this data state system was created, and then it is discarded. For that
    /// reason, it should NEVER be used as a source of information.
    protected ItemDataState<I> itemDataState;

    /// Represents the translation key for this item entry generated using its
    /// [#resourceKey()].
    public String descriptionId() {
        return ((ItemPropertiesExtension) properties).regy$getEffectiveDescriptionId();
    }

    public ItemDataState<I> getDataState() {
        return itemDataState;
    }

    @Override
    public void collectDataGenProviders(DataGenProviderConsumer collector) {
        collector.addProvider(this::dataGenLangProvider);
        collector.addProvider(this::dataGenModelProvider);
        collector.addProvider(this::dataGenItemTagProvider);
        collector.addProvider(this::dataGenRecipeProvider);

        this.onCollectProviders.accept(collector);
    }

    public ItemEntryBuilder<I, P> collectCustomProviders(@NonNull Consumer<@NonNull DataGenProviderConsumer> consumer) {
        this.onCollectProviders.subscribe(consumer);
        return this;
    }

    protected void dataGenRecipeProvider(DataGeneration gen) {
        gen.<RecipeDataGenerator>getGeneratorOptional(DataGenerators.RECIPES).ifPresent(recipes ->
                this.recipeStorage.forEach(instruction ->
                        recipes.registerRecipe((provider, output) ->
                                instruction.generateRecipe(gen, provider, output, this.itemDataState))
                )
        );
    }

    protected void dataGenItemTagProvider(DataGeneration gen) {
        gen.<ItemTagDataGenerator>getGeneratorOptional(DataGenerators.ITEM_TAGS).ifPresent(tags -> {
            synchronized (this.tagStorage) {
                this.tagStorage.forEach(tag -> tags.tag(
                        tag,
                        builder -> builder.add(itemDataState.item()).setReplace(false)
                ));
            }
        });
    }

    protected void dataGenModelProvider(DataGeneration gen) {
        if (this.modelInstruction == null) {
            return;
        }

        gen.<ModelDataGenerator>getGeneratorOptional(DataGenerators.MODELS).ifPresent(models ->
                models.addItemModel(() -> itemModelGenerator ->
                        modelInstruction.generateModel(gen, itemModelGenerator, itemDataState)
                )
        );
    }

    protected void dataGenLangProvider(DataGeneration gen) {
        gen.<LangDataGenerator>getGeneratorOptional(DataGenerators.LANG).ifPresent(lang ->
                lang.add(descriptionId(), this.generatedName)
        );
    }

    @FunctionalInterface
    public interface ModelInstruction<I extends Item> {
        void generateModel(DataGeneration dataGen, ItemModelGenerators generators, ItemDataState<I> state);

        static <I extends Item> ModelInstruction<I> simple() {
            return (dataGen, generators, state) ->
                    generators.generateFlatItem(state.item(), ModelTemplates.FLAT_ITEM);
        }

        static <I extends Item> ModelInstruction<I> handheld(ItemModel.Unbaked gui, ItemModel.Unbaked handheld) {
            return (dataGen, generators, state) ->
                    generators.itemModelOutput.accept(state.item(), ItemModelGenerators.createFlatModelDispatch(gui, handheld));
        }
    }

    @FunctionalInterface
    public interface Recipe<I extends Item> {
        void generateRecipe(DataGeneration dataGeneration, RecipeProvider provider, RecipeOutput output, ItemDataState<I> state);
    }

    // endregion

    // region modifiers

    // TODO: Rename to transform
    public <B> B transform(Function<ItemEntryBuilder<I, P>, B> mapper) {
        return mapper.apply(this);
    }

    public ItemEntryBuilder<I, P> name(@NonNull String name) {
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

    public ItemEntryBuilder<I, P> model(@NonNull ModelInstruction<I> instruction) {
        this.modelInstruction = instruction;
        return this;
    }

    public ItemEntryBuilder<I, P> customModel() {
        this.modelInstruction = null;
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

    public ItemEntryBuilder<I, P> tag(@NonNull TagKey<Item> key) {
        tagStorage.add(key);
        return this;
    }

    public ItemEntryBuilder<I, P> recipe(Recipe<I> recipe) {
        this.recipeStorage.add(recipe);
        return this;
    }

    // endregion
}

