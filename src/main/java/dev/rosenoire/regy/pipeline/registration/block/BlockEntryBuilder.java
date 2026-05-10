package dev.rosenoire.regy.pipeline.registration.block;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.datagen.DataGenObject;
import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.DataGenerators;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.ModelDataGenerator;
import dev.rosenoire.regy.pipeline.factory.BlockFactory;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntryBuilder;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class BlockEntryBuilder<B extends Block, P> extends AbstractEntryBuilder<BlockEntry<B>, P> implements DataGenObject {
    /// Factory used to create the final instance of [B] of the to-be-registered [BlockEntry].
    protected final BlockFactory<B> factory;
    /// [ResourceKey] of [Block] representing the key that will be used to register the entry.
    protected final ResourceKey<Block> resourceKey;
    /// [BlockBehaviour.Properties] used to register the [BlockEntry].
    protected BlockBehaviour.@NonNull Properties properties;
    protected @Nullable ItemEntryBuilder<?, BlockEntryBuilder<B, P>> child;
    protected @NonNull BlockRenderMode renderMode = BlockRenderMode.SOLID;

    public BlockEntryBuilder(AbstractRegy<?> regy, P parent, String identifier, BlockFactory<B> factory) {
        super(regy, parent, identifier);

        this.factory = factory;
        this.resourceKey = ResourceKey.create(Registries.BLOCK, identifier());
        this.properties = BlockBehaviour.Properties.of();

        this.cubeModel();
    }

    @Override
    public @NonNull BlockEntry<B> register() {
        this.properties.setId(resourceKey);

        var instance = this.factory.bake(this.properties);
        Registry.register(BuiltInRegistries.BLOCK, resourceKey, instance);

        var entry = new BlockEntry<>(instance, resourceKey, renderMode);
        entry = getRegy().entry(entry);

        this.dataState = new BlockDataState<>(instance, resourceKey, this.path());
        this.getRegy().dataGeneration().addData(this);

        if (child != null) {
            child.register();
        }

        return entry;
    }

    @Override
    protected Identifier regyIdentifier() {
        return getRegyIdentifierFromRegistry(this.resourceKey);
    }

    // region modifiers

    public <A> A transform(Function<BlockEntryBuilder<B, P>, A> transformer) {
        return transformer.apply(this);
    }

    // region rendering

    public BlockEntryBuilder<B, P> renderMode(@NonNull BlockRenderMode renderMode) {
        this.renderMode = renderMode;
        return this;
    }

    public BlockEntryBuilder<B, P> solid() {
        return renderMode(BlockRenderMode.SOLID);
    }

    public BlockEntryBuilder<B, P> cutout() {
        return renderMode(BlockRenderMode.CUTOUT);
    }

    public BlockEntryBuilder<B, P> translucent() {
        return renderMode(BlockRenderMode.TRANSLUCENT);
    }

    // endregion

    // region properties

    public BlockEntryBuilder<B, P> properties(BlockBehaviour.@NonNull Properties properties) {
        this.properties = properties;
        return this;
    }

    public BlockEntryBuilder<B, P> properties(UnaryOperator<BlockBehaviour.@NonNull Properties> function) {
        return this.properties(function.apply(this.properties));
    }

    public BlockEntryBuilder<B, P> properties(BlockBehaviour source) {
        return this.properties(BlockBehaviour.Properties.ofFullCopy(source));
    }

    // endregion

    // region item

    public <I extends Item> ItemEntryBuilder<I, BlockEntryBuilder<B, P>> item(BlockItemFactory<B, I> factory) {
        ItemEntryBuilder<I, BlockEntryBuilder<B, P>> builder = this.getRegy()
                .item(
                        path(),
                        this,
                        (entryBuilder, properties) ->
                                factory.blockItem(this.entry().get().get(), properties)
                );

        builder
                .properties(Item.Properties::useBlockDescriptionPrefix)
                .simpleName()
                .customModel();

        this.child = builder;
        return builder;
    }

    public ItemEntryBuilder<BlockItem, BlockEntryBuilder<B, P>> item() {
        return item(BlockItem::new);
    }

    public BlockEntryBuilder<B, P> simpleItem() {
        return item().build();
    }

    // endregion

    // region data generation

    public BlockEntryBuilder<B, P> model(@NonNull ModelInstruction<B> modelInstruction) {
        this.modelInstruction = modelInstruction;
        return this;
    }

    public BlockEntryBuilder<B, P> cubeModel() {
        return basicModel(BlockModelGenerators::createTrivialCube);
    }

    public BlockEntryBuilder<B, P> pillarModel() {
        return basicModel((generators, block) ->
                generators.createAxisAlignedPillarBlock(block, TexturedModel.COLUMN)
        );
    }

    public BlockEntryBuilder<B, P> barsModel() {
        return basicModel(BlockModelGenerators::createBarsAndItem);
    }

    public BlockEntryBuilder<B, P> chainsModel() {
        return basicModel((generators, block) -> {
            generators.registerSimpleFlatItemModel(block.asItem());
            var variant = BlockModelGenerators.plainVariant(TexturedModel.CHAIN.create(block, generators.modelOutput));
            generators.createAxisAlignedPillarBlockCustomModel(block, variant);
        });
    }

    public BlockEntryBuilder<B, P> basicModel(BiConsumer<BlockModelGenerators, B> biConsumer) {
        return this.model((dataGen, generators, state) -> {
            biConsumer.accept(generators, state.block);
        });
    }

    public BlockEntryBuilder<B, P> customModel() {
        this.modelInstruction = null;
        return this;
    }

    // endregion

    // endregion

    // region data generation

    protected @Nullable ModelInstruction<B> modelInstruction;
    protected BlockDataState<B> dataState;

    @Override
    public void collectDataGenProviders(@NonNull DataGenProviderConsumer collector) {
        if (this.modelInstruction != null) {
            collector.addProvider(this::modelDataGenProvider);
        }
    }

    private void modelDataGenProvider(@NonNull DataGeneration dataGeneration) {
        dataGeneration.<ModelDataGenerator>getGeneratorOptional(DataGenerators.MODELS).ifPresent(generator -> {
            this.generateModels(dataGeneration, generator);
        });
    }

    private void generateModels(@NonNull DataGeneration dataGeneration, @NonNull ModelDataGenerator generator) {
        assert this.modelInstruction != null;

        generator.addBlockModel(() -> blockGenerators -> {
            this.modelInstruction.generateModel(dataGeneration, blockGenerators, this.dataState);
        });
    }

    public @FunctionalInterface interface ModelInstruction<B extends Block> {
        void generateModel(@NonNull DataGeneration dataGen, @NonNull BlockModelGenerators generators, @NonNull BlockDataState<B> state);
    }

    public record BlockDataState<B extends Block>(@NonNull B block, @NonNull ResourceKey<Block> resourceKey, @NonNull String name) {
    }

    // endregion
}