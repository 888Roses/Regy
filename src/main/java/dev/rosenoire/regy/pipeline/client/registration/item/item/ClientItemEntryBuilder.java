package dev.rosenoire.regy.pipeline.client.registration.item.item;

import dev.rosenoire.regy.api.event.ValueEvent;
import dev.rosenoire.regy.api.text.NamingConventions;
import dev.rosenoire.regy.common.index.InternalDataComponents;
import dev.rosenoire.regy.pipeline.client.AbstractClientRegy;
import dev.rosenoire.regy.pipeline.client.registration.AbstractClientEntryBuilder;
import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.*;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import dev.rosenoire.regy.tooltips.builder.TooltipBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@Environment(EnvType.CLIENT)
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ClientItemEntryBuilder<I extends Item> extends AbstractClientEntryBuilder<ItemEntry<I>, I> {
    protected final ValueEvent<@NonNull DataGenProviderConsumer> onCollectProviders = new ValueEvent<>();

    protected final List<RecipeInstruction<I>> recipeStorage = new ArrayList<>();
    protected final List<TagKey<Item>> tagStorage = new ArrayList<>();

    protected @Nullable String generatedName;
    protected @Nullable ModelInstruction<I> modelInstruction = ModelInstruction.simple();
    protected @Nullable UnaryOperator<TooltipBuilder> tooltipBuilder;

    protected final Map<String, String> skinNames = new HashMap<>();
    protected final Map<String, String> skinAuthors = new HashMap<>();

    public ClientItemEntryBuilder(@NonNull AbstractClientRegy<?, ?> client, @NonNull ItemEntry<I> entry) {
        super(client, entry);

        this.simpleName();
        this.simpleModel();
    }

    public ClientItemEntryBuilder<I> skinName(String skin, String displayName) {
        this.skinNames.put(skin, displayName);
        return this;
    }

    public ClientItemEntryBuilder<I> skinAuthor(String skin, String author) {
        this.skinAuthors.put(skin, author);
        return this;
    }

    public ClientItemEntryBuilder<I> skin(String skin, String displayName, String author) {
        return this.skinName(skin, displayName).skinAuthor(skin, author);
    }

    @Override
    public void collectDataGenProviders(DataGenProviderConsumer collector) {
        collector.addProvider(this::dataGenLangProvider);
        collector.addProvider(this::dataGenModelProvider);
        collector.addProvider(this::dataGenItemTagProvider);
        collector.addProvider(this::dataGenRecipeProvider);

        this.onCollectProviders.accept(collector);
    }

    public ClientItemEntryBuilder<I> onCollectProviders(@NonNull Consumer<@NonNull DataGenProviderConsumer> event) {
        this.onCollectProviders.subscribe(event);
        return this;
    }

    protected void dataGenRecipeProvider(DataGeneration gen) {
        gen.<RecipeDataGenerator>getGeneratorOptional(DataGenerators.RECIPES).ifPresent(recipes -> this.recipeStorage.forEach(instruction -> recipes.registerRecipe((provider, output) -> instruction.generateRecipe(gen, provider, output, this.entry()))));
    }

    protected void dataGenItemTagProvider(DataGeneration gen) {
        gen.<ItemTagDataGenerator>getGeneratorOptional(DataGenerators.ITEM_TAGS).ifPresent(tags -> {
            synchronized (this.tagStorage) {
                this.tagStorage.forEach(tag -> tags
                        .tag(tag, builder -> builder
                                .add(this.value())
                                .setReplace(false)
                        )
                );
            }
        });
    }

    protected void dataGenModelProvider(DataGeneration gen) {
        if (this.modelInstruction == null) {
            return;
        }

        gen.<ModelDataGenerator>getGeneratorOptional(DataGenerators.MODELS).ifPresent(models -> models.addItemModel(() -> itemModelGenerator -> modelInstruction.generateModel(gen, itemModelGenerator, this.entry())));
    }

    protected void dataGenLangProvider(DataGeneration gen) {
        // No need to generate any lang datagen if both the name and the tooltip are
        // null (which means that nothing would be generated anyway).
        if (this.generatedName == null && this.tooltipBuilder == null) {
            return;
        }

        gen.<LangDataGenerator>getGeneratorOptional(DataGenerators.LANG).ifPresent(lang -> {
            var desc = this.value().getDescriptionId();
            lang.add(desc, this.generatedName);

            if (this.tooltipBuilder != null) {
                this.tooltipBuilder.apply(new TooltipBuilder(desc, lang::add)).build();
            }

            var component = InternalDataComponents.ITEM_SKIN.get(this.value());

            if (component != null) {
                for (var skin : component.skins()) {
                    var nameId = desc + ".skin." + skin;
                    lang.add(nameId, this.skinNames.getOrDefault(skin, NamingConventions.HUMAN_TEXT.transform(skin)));

                    if (this.skinAuthors.containsKey(skin)) {
                        var authorId = nameId + ".author";
                        lang.add(authorId, this.skinAuthors.get(skin));
                    }
                }
            }
        });
    }

    public ClientItemEntryBuilder<I> name(@NonNull String name) {
        this.generatedName = name;
        return this;
    }

    public ClientItemEntryBuilder<I> simpleName() {
        return name(NamingConventions.HUMAN_TEXT.transform(path()));
    }

    public ClientItemEntryBuilder<I> customName() {
        this.generatedName = null;
        return this;
    }

    public ClientItemEntryBuilder<I> tooltip(@NonNull UnaryOperator<TooltipBuilder> builder) {
        this.tooltipBuilder = builder;
        return this;
    }

    public ClientItemEntryBuilder<I> noTooltip() {
        this.tooltipBuilder = null;
        return this;
    }

    public ClientItemEntryBuilder<I> model(@NonNull ModelInstruction<I> instruction) {
        this.modelInstruction = instruction;
        return this;
    }

    public ClientItemEntryBuilder<I> simpleSkinModel() {
        return this.skinModel(SkinModelInstruction.simple());
    }

    public ClientItemEntryBuilder<I> simpleSkinModel(@NonNull ModelTemplate modelTemplate) {
        return this.skinModel(SkinModelInstruction.simple(modelTemplate));
    }

    public ClientItemEntryBuilder<I> skinModel(@NonNull SkinModelInstruction<I> instruction) {
        return this.model(instruction.compute());
    }

    public ClientItemEntryBuilder<I> customModel() {
        this.modelInstruction = null;
        return this;
    }

    public ClientItemEntryBuilder<I> simpleModel() {
        return this.model(ModelInstruction.simple());
    }

    public ClientItemEntryBuilder<I> handheldModel(ItemModel.@NonNull Unbaked modelInGui, ItemModel.@NonNull Unbaked modelInWorld) {
        return this.model(ModelInstruction.handheld(modelInGui, modelInWorld));
    }

    public ClientItemEntryBuilder<I> handheldModel(@NonNull String modelInGuiSuffix, @NonNull String modelInWorldSuffix) {
        return this.model(ModelInstruction.plainHandheld(this.identifier(), modelInGuiSuffix, modelInGuiSuffix));
    }

    public ClientItemEntryBuilder<I> handheldModel() {
        return this.model(ModelInstruction.plainHandheld(this.identifier()));
    }

    public ClientItemEntryBuilder<I> tag(@NonNull TagKey<Item> key) {
        tagStorage.add(key);
        return this;
    }

    public ClientItemEntryBuilder<I> recipe(RecipeInstruction<I> recipe) {
        this.recipeStorage.add(recipe);
        return this;
    }
}