package dev.rosenoire.regy.pipeline.datagen.impl.generator;

import dev.rosenoire.regy.pipeline.datagen.DataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class FluidTagDataGenerator extends FabricTagProvider.FluidTagProvider implements TagDataGenerator<Fluid, FluidTagDataGenerator> {
    private final Map<TagKey<Fluid>, UnaryOperator<TagAppender<Fluid, Fluid>>> tagStorage = new HashMap<>();

    public FluidTagDataGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        this.tagStorage.forEach((tag, func) -> func.apply(valueLookupBuilder(tag)));
    }

    @Override
    public FluidTagDataGenerator tag(TagKey<Fluid> tag, UnaryOperator<TagAppender<Fluid, Fluid>> func) {
        this.tagStorage.put(tag, func);
        return this;
    }

    @Override
    public @NonNull String getName() {
        return DataGenerators.FLUID_TAGS;
    }
}