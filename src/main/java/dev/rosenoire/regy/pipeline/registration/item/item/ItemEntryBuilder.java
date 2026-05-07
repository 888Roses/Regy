package dev.rosenoire.regy.pipeline.registration.item.item;

import dev.rosenoire.regy.api.data.RegistryUtils;
import dev.rosenoire.regy.api.model.ModelUtils;
import dev.rosenoire.regy.api.text.NamingConventions;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.datagen.provider.lang.DatagenTranslatable;
import dev.rosenoire.regy.pipeline.datagen.provider.model.DatagenModelTarget;
import dev.rosenoire.regy.pipeline.datagen.provider.model.ItemModelInstruction;
import dev.rosenoire.regy.pipeline.factory.ItemFactory;
import dev.rosenoire.regy.pipeline.registration.AbstractEntry;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.group.CreativeTabEntry;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUtils;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@SuppressWarnings("UnusedReturnValue")
public class ItemEntryBuilder<I extends Item, P> extends AbstractEntryBuilder<ItemEntry<I>, P> implements DatagenTranslatable, DatagenModelTarget.ItemTarget {
    private final ItemFactory<I> factory;
    private final ResourceKey<Item> resourceKey;
    private Item.Properties properties;
    private @Nullable String generatedName;
    private @Nullable CreativeTabEntry creativeTab;
    private @NonNull ItemModelInstruction currentInstruction = ItemModelInstruction.SIMPLE;

    public ItemEntryBuilder(@NonNull AbstractRegy<?> owner, P parent, String identifier, ItemFactory<I> factory) {
        super(owner, parent, identifier);
        this.factory = factory;
        this.resourceKey = ResourceKey.create(Registries.ITEM, identifier());
        this.properties = new Item.Properties().setId(resourceKey);

        this.simpleName();
        this.simpleModel();
    }

    @Override
    public @NonNull ItemEntry<I> register() {
        return Optional.of(factory.bake(this.properties))
                .map(item -> Registry.register(BuiltInRegistries.ITEM, resourceKey, item))
                .map(registered -> new ItemEntry<>(registered, resourceKey))
                .map(entry -> getOwner().process(this, entry))
                .orElseThrow(this::throwRegisterNullEntryException);
    }

    // region modifiers

    public <T extends AbstractEntry<?, ?>, V, B extends AbstractEntryBuilder<T, V>> B transform(Function<ItemEntryBuilder<I, P>, B> transformer) {
        return transformer.apply(this);
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

    public ItemEntryBuilder<I, P> customModel(ItemModelInstruction instruction) {
        this.currentInstruction = instruction;
        return this;
    }

    public ItemEntryBuilder<I, P> simpleModel() {
        return customModel(ItemModelInstruction.SIMPLE);
    }

    public ItemEntryBuilder<I, P> handheldModel(ItemModel.Unbaked gui, ItemModel.Unbaked handheld) {
        return customModel(ItemModelInstruction.HANDHELD_MODEL.apply(gui, handheld));
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

    // endregion

    // region processing

    @Override
    public Translation getDatagenTranslation() {
        return new Translation(RegistryUtils.toDescriptionId(resourceKey), this.generatedName);
    }

    public @Nullable CreativeTabEntry getCreativeTab() {
        return creativeTab;
    }

    @Override
    public ItemModelInstruction getInstruction() {
        return currentInstruction;
    }

    // endregion
}