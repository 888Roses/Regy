package dev.rosenoire.regy.pipeline.client;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.datagen.impl.adapter.DefaultAdapters;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.*;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.sound.SoundDataGenerator;
import dev.rosenoire.regy.pipeline.datagen.impl.post_processor.DefaultPostProcessors;
import dev.rosenoire.regy.pipeline.datagen.post_processor.DataPostProcessor;
import org.jspecify.annotations.NonNull;

public class ClientRegy<R extends AbstractRegy<R>> extends AbstractClientRegy<R, ClientRegy<R>> {
    protected ClientRegy(@NonNull R instance) {
        super(instance);

        this
                .registerEntryBuilderAdapter(DefaultAdapters.ITEM)
                .registerEntryBuilderAdapter(DefaultAdapters.BLOCK)
                .registerEntryBuilderAdapter(DefaultAdapters.POTION)
                .registerEntryBuilderAdapter(DefaultAdapters.SOUND);

        this.dataGeneration()
                .addGenerator(LangDataGenerator::new)
                .addGenerator(ModelDataGenerator::new)
                .addGenerator(RecipeDataGenerator::new)
                .addGenerator(ItemTagDataGenerator::new)
                .addGenerator(BlockTagDataGenerator::new)
                .addGenerator(BlockEntityTagDataGenerator::new)
                .addGenerator(FluidTagDataGenerator::new)
                .addGenerator(EntityTypeTagDataGenerator::new)
                .addGenerator(SoundDataGenerator::new);

        this.dataGeneration()
                .registerPostProcessor(DefaultPostProcessors.TAG);
    }

    public static <R extends AbstractRegy<R>> ClientRegy<R> create(@NonNull R instance) {
        return new ClientRegy<>(instance);
    }
}
