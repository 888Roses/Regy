package dev.rosenoire.regy.pipeline.registration.item.item;

import dev.rosenoire.regy.api.data.NonNullSupplier;
import dev.rosenoire.regy.api.data.RegistryUtils;
import dev.rosenoire.regy.api.model.ModelUtils;
import dev.rosenoire.regy.api.text.NamingConventions;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.datagen.v1.provider.recipe.RecipeInstruction;
import dev.rosenoire.regy.pipeline.datagen.v2.DataGenObject;
import dev.rosenoire.regy.pipeline.datagen.v2.DataGeneration;
import dev.rosenoire.regy.pipeline.datagen.v2.impl.generator.ItemTagDataGenerator;
import dev.rosenoire.regy.pipeline.datagen.v2.impl.generator.LangDataGenerator;
import dev.rosenoire.regy.pipeline.datagen.v2.impl.generator.ModelDataGenerator;
import dev.rosenoire.regy.pipeline.factory.ItemFactory;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.group.CreativeTabEntry;
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
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
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
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@SuppressWarnings("UnusedReturnValue")
public class ItemEntryBuilder<I extends Item, P> extends AbstractEntryBuilder<ItemEntry<I>, P> implements DataGenObject {
    private final ItemFactory<I> itemFactory;
    private final ResourceKey<Item> resourceKey;
    private Item.Properties properties;
    private @Nullable String generatedName;
    private @Nullable CreativeTabEntry creativeTab;
    private @NonNull ModelInstruction<I> modelInstruction = ModelInstruction.simple();
    private @Nullable ToolMaterial toolMaterial;
    private NonNullSupplier<ItemAttributeModifiers.Builder> attributesBuilder = null;
    private final List<TagKey<Item>> tagStorage = new ArrayList<>();
    private final List<RecipeInstruction> recipeStorage = new ArrayList<>();

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
        if (this.attributesBuilder != null) {
            this.properties = this.properties.attributes(this.attributesBuilder.get().build());
        }

        var item = itemFactory.bake(this.properties);
        Registry.register(BuiltInRegistries.ITEM, resourceKey, item);

        dataGenData = new DatagenData<I>(item);
        getRegy().dataGeneration().addData(this);

        var entry = new ItemEntry<>(item, resourceKey, toolMaterial());
        return getRegy().entry(entry);
    }

    // region accessors

    public ResourceKey<Item> resourceKey() {
        return this.resourceKey;
    }

    public @Nullable CreativeTabEntry creativeTab() {
        return creativeTab;
    }

    public @Nullable ToolMaterial toolMaterial() {
        return toolMaterial;
    }

    // endregion

    // region datagen
    private DatagenData<I> dataGenData;

    public String descriptionId() {
        return RegistryUtils.toDescriptionId(this.resourceKey());
    }

    @Override
    public void collectDataGenProviders(DataGenProviderConsumer collector) {
        collector.addProvider(dataGen ->
                dataGen.<LangDataGenerator>getGeneratorOptional("lang").ifPresent(generator ->
                        generator.add(descriptionId(), this.generatedName)
                )
        );

        collector.addProvider(dataGen ->
                dataGen.<ModelDataGenerator>getGeneratorOptional("model").ifPresent(generator ->
                        generator.addItemModel(() -> itemModelGenerator ->
                                modelInstruction.generateModel(dataGen, itemModelGenerator, dataGenData)
                        )
                )
        );

        collector.addProvider(dataGen ->
                dataGen.<ItemTagDataGenerator>getGeneratorOptional("item_tag").ifPresent(generator -> {
                            for (var tag : this.tagStorage) {
                                generator.tag(tag, builder -> builder
                                        .add(dataGenData.item())
                                        .setReplace(false)
                                );
                            }
                        }
                )
        );
    }

    public record DatagenData<I extends Item>(I item) {
    }

    @FunctionalInterface
    public interface ModelInstruction<I extends Item> {
        void generateModel(DataGeneration dataGen, ItemModelGenerators generators, DatagenData<I> data);

        static <I extends Item> ModelInstruction<I> simple() {
            return (dataGen, generators, data) -> {
                generators.generateFlatItem(data.item(), ModelTemplates.FLAT_ITEM);
            };
        }

        static <I extends Item> ModelInstruction<I> handheld(ItemModel.Unbaked gui, ItemModel.Unbaked handheld) {
            return (dataGen, generators, data) -> {
                generators.itemModelOutput.accept(data.item(), ItemModelGenerators.createFlatModelDispatch(gui, handheld));
            };
        }
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

    public ItemEntryBuilder<I, P> customModel(ModelInstruction instruction) {
        this.modelInstruction = instruction;
        return this;
    }

    public ItemEntryBuilder<I, P> simpleModel() {
        return customModel(ModelInstruction.simple());
    }

    public ItemEntryBuilder<I, P> handheldModel(ItemModel.Unbaked gui, ItemModel.Unbaked handheld) {
        return customModel(ModelInstruction.handheld(gui, handheld));
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
            this.attributesBuilder = ItemAttributeModifiers::builder;
        }

        this.attributesBuilder = this.attributesBuilder.map(current -> current.add(entry.attribute(), entry.modifier(), entry.slot(), entry.display()));
        return this;
    }

    public ItemEntryBuilder<I, P> attribute(Holder<Attribute> holder, AttributeModifier attributeModifier, EquipmentSlotGroup equipmentSlotGroup) {
        return this.attribute(new ItemAttributeModifiers.Entry(holder, attributeModifier, equipmentSlotGroup));
    }

    public ItemEntryBuilder<I, P> attribute(Holder<Attribute> holder, AttributeModifier attributeModifier, EquipmentSlotGroup equipmentSlotGroup, ItemAttributeModifiers.Display display) {
        return this.attribute(new ItemAttributeModifiers.Entry(holder, attributeModifier, equipmentSlotGroup, display));
    }

    public ItemEntryBuilder<I, P> toolMaterial(ToolMaterial toolMaterial) {
        this.toolMaterial = toolMaterial;
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

    public ItemEntryBuilder<I, P> tool(Function<HolderGetter<Block>, Tool> func) {
        return map(builder -> {
            HolderGetter<Block> blockRegistry = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
            return builder.component(DataComponents.TOOL, func.apply(blockRegistry));
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

    public SwordSettingsBuilder<I, P> swordSettings() {
        return new SwordSettingsBuilder<>(this);
    }

    public ItemEntryBuilder<I, P> tag(TagKey<Item> key) {
        tagStorage.add(key);
        return this;
    }

    // endregion
}