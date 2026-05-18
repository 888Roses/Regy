package dev.rosenoire.regy.pipeline.client.registration.item.item;

import dev.rosenoire.regy.api.event.ValueEvent;
import dev.rosenoire.regy.api.model.ModelUtils;
import dev.rosenoire.regy.api.text.NamingConventions;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.client.registration.AbstractClientEntryBuilder;
import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.*;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import dev.rosenoire.regy.tooltips.builder.TooltipBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
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

    public ClientItemEntryBuilder(@NonNull AbstractRegy<?> regy, @NonNull ItemEntry<I> entry) {
        super(regy, entry);

        this.simpleName();
        this.simpleModel();
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
                this.tagStorage.forEach(tag -> tags.tag(tag, builder -> builder.add(this.value()).setReplace(false)));
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

    public ClientItemEntryBuilder<I> customModel() {
        this.modelInstruction = null;
        return this;
    }

    public ClientItemEntryBuilder<I> simpleModel() {
        return model(ModelInstruction.simple());
    }

    public ClientItemEntryBuilder<I> handheldModel(net.minecraft.client.renderer.item.ItemModel.Unbaked gui, net.minecraft.client.renderer.item.ItemModel.Unbaked handheld) {
        return model(ModelInstruction.handheld(gui, handheld));
    }

    public ClientItemEntryBuilder<I> handheldModel(String guiSuffix, String handheldSuffix) {
        return this.handheldModel(ItemModelUtils.plainModel(ModelUtils.getItemSubModelId(identifier(), guiSuffix)), ItemModelUtils.plainModel(ModelUtils.getItemSubModelId(identifier(), handheldSuffix)));
    }

    public ClientItemEntryBuilder<I> handheldModel() {
        return handheldModel("", "handheld");
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