package dev.rosenoire.regy.pipeline.client.registration.block;

import dev.rosenoire.regy.api.event.ValueEvent;
import dev.rosenoire.regy.foundation.extensions.StairBlockExtension;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.client.registration.AbstractClientEntryBuilder;
import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.BlockTagDataGenerator;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.DataGenerators;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.ModelDataGenerator;
import dev.rosenoire.regy.pipeline.registration.block.BlockEntry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class ClientBlockEntryBuilder<B extends Block> extends AbstractClientEntryBuilder<BlockEntry<B>, B> {

    public ClientBlockEntryBuilder(@NonNull AbstractRegy<?> regy, @NonNull BlockEntry<B> entry) {
        super(regy, entry);

        this.cubeModel();
    }

    // region models

    public ClientBlockEntryBuilder<B> model(@NonNull ModelInstruction<B> modelInstruction) {
        this.modelInstruction = modelInstruction;
        return this;
    }

    public ClientBlockEntryBuilder<B> cubeModel() {
        return basicModel(BlockModelGenerators::createTrivialCube);
    }

    public ClientBlockEntryBuilder<B> pillarModel() {
        return basicModel((generators, block) ->
                generators.createAxisAlignedPillarBlock(block, TexturedModel.COLUMN)
        );
    }

    public ClientBlockEntryBuilder<B> stairsModel() {
        return basicModel((generators, block) -> {
            if (block instanceof StairBlockExtension stairBlock) {
                var texturedModel = TexturedModel.CUBE.get(stairBlock.regy$getBaseState().getBlock());
                var mapping = texturedModel.getMapping();
                var stairsInner = BlockModelGenerators.plainVariant(ModelTemplates.STAIRS_INNER.create(block, mapping, generators.modelOutput));
                var stairsStraight = ModelTemplates.STAIRS_STRAIGHT.create(block, mapping, generators.modelOutput);
                var stairsOuter = BlockModelGenerators.plainVariant(ModelTemplates.STAIRS_OUTER.create(block, mapping, generators.modelOutput));
                generators.blockStateOutput.accept(BlockModelGenerators.createStairs(block, stairsInner, BlockModelGenerators.plainVariant(stairsStraight), stairsOuter));
                generators.registerSimpleItemModel(block, stairsStraight);
            } else {
                throw new IllegalStateException("Cannot generate stairs model for block that doesn't extend StairBlock! " + block);
            }
        });
    }

    public ClientBlockEntryBuilder<B> slabModel(Block source) {
        return basicModel((generators, block) -> {
            var textureMapping = TextureMapping.cube(source);
            var full = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(source));
            var bottom = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_BOTTOM.create(block, textureMapping, generators.modelOutput));
            var top = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_TOP.create(block, textureMapping, generators.modelOutput));
            generators.blockStateOutput.accept(BlockModelGenerators.createSlab(block, bottom, top, full));
        });
    }

    public ClientBlockEntryBuilder<B> slabModel(BlockEntry<?> source) {
        return slabModel(source.get());
    }

    public ClientBlockEntryBuilder<B> wallModel(Block source) {
        return basicModel((generators, block) -> {
            var texturedModel = TexturedModel.CUBE.get(source);
            var mapping = texturedModel.getMapping();

            var post = BlockModelGenerators.plainVariant(ModelTemplates.WALL_POST.create(block, mapping, generators.modelOutput));
            var lowSide = BlockModelGenerators.plainVariant(ModelTemplates.WALL_LOW_SIDE.create(block, mapping, generators.modelOutput));
            var tallSide = BlockModelGenerators.plainVariant(ModelTemplates.WALL_TALL_SIDE.create(block, mapping, generators.modelOutput));
            generators.blockStateOutput.accept(BlockModelGenerators.createWall(block, post, lowSide, tallSide));

            var inventory = ModelTemplates.WALL_INVENTORY.create(block, mapping, generators.modelOutput);
            generators.registerSimpleItemModel(block, inventory);
        });
    }

    public ClientBlockEntryBuilder<B> wallModel(BlockEntry<?> source) {
        return wallModel(source.get());
    }

    public ClientBlockEntryBuilder<B> barsModel() {
        return basicModel(BlockModelGenerators::createBarsAndItem);
    }

    public ClientBlockEntryBuilder<B> trapdoorModel(boolean isOrientable) {
        return basicModel((generators, block) -> {
            if (isOrientable) {
                generators.createOrientableTrapdoor(block);
                return;
            }

            generators.createTrapdoor(block);
        });
    }

    public ClientBlockEntryBuilder<B> doorModel() {
        return basicModel(BlockModelGenerators::createDoor);
    }

    public ClientBlockEntryBuilder<B> trapdoorModel() {
        return trapdoorModel(true);
    }

    public ClientBlockEntryBuilder<B> chainsModel() {
        return basicModel((generators, block) -> {
            generators.registerSimpleFlatItemModel(block.asItem());
            var variant = BlockModelGenerators.plainVariant(TexturedModel.CHAIN.create(block, generators.modelOutput));
            generators.createAxisAlignedPillarBlockCustomModel(block, variant);
        });
    }

    public ClientBlockEntryBuilder<B> basicModel(BiConsumer<BlockModelGenerators, B> biConsumer) {
        return this.model((dataGen, generators, entry) -> {
            biConsumer.accept(generators, entry.get());
        });
    }

    public ClientBlockEntryBuilder<B> customModel() {
        this.modelInstruction = null;
        return this;
    }

    // endregion

    public ClientBlockEntryBuilder<B> collectCustomProviders(@NonNull Consumer<@NonNull DataGenProviderConsumer> consumer) {
        this.onCollectProviders.subscribe(consumer);
        return this;
    }

    protected @Nullable ModelInstruction<B> modelInstruction;
    protected final ValueEvent<@NonNull DataGenProviderConsumer> onCollectProviders = new ValueEvent<>();

    @Override
    public void collectDataGenProviders(@NonNull DataGenProviderConsumer collector) {
        if (this.modelInstruction != null) {
            collector.addProvider(this::modelDataGenProvider);
        }

        if (!this.entry().tagStorage.isEmpty()) {
            collector.addProvider(this::tagDataGenProvider);
        }

        this.onCollectProviders.accept(collector);
    }

    private void tagDataGenProvider(DataGeneration dataGeneration) {
        dataGeneration.<BlockTagDataGenerator>getGeneratorOptional(DataGenerators.BLOCK_TAGS).ifPresent(generator -> {
            synchronized (this.entry().tagStorage) {
                this.entry().tagStorage.forEach(tag -> {
                    generator.tag(tag, builder -> builder.add(this.value()).setReplace(false));
                });
            }
        });
    }

    private void modelDataGenProvider(@NonNull DataGeneration dataGeneration) {
        dataGeneration.<ModelDataGenerator>getGeneratorOptional(DataGenerators.MODELS).ifPresent(generator -> {
            this.generateModels(dataGeneration, generator);
        });
    }

    private void generateModels(@NonNull DataGeneration dataGeneration, @NonNull ModelDataGenerator generator) {
        assert this.modelInstruction != null;

        generator.addBlockModel(() -> blockGenerators -> {
            this.modelInstruction.generateModel(dataGeneration, blockGenerators, this.entry());
        });
    }

    public @FunctionalInterface interface ModelInstruction<B extends Block> {
        void generateModel(@NonNull DataGeneration dataGen, @NonNull BlockModelGenerators generators, @NonNull BlockEntry<B> blockEntry);
    }
}
