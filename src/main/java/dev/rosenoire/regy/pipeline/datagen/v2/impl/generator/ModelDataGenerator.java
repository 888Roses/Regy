package dev.rosenoire.regy.pipeline.datagen.v2.impl.generator;

import dev.rosenoire.regy.api.data.NonNullSupplier;
import dev.rosenoire.regy.pipeline.datagen.v2.DataGenerator;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModelDataGenerator extends FabricModelProvider implements DataGenerator {
    private final List<Consumer<ItemModelGenerators>> itemInstructionStorage = new ArrayList<>();

    public ModelDataGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NonNull BlockModelGenerators blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerators itemModelGenerator) {
        for (var instruction : this.itemInstructionStorage) {
            instruction.accept(itemModelGenerator);
        }
    }

    // TODO: Documentation!
    public ModelDataGenerator addItemModel(NonNullSupplier<Consumer<ItemModelGenerators>> consumer) {
        this.itemInstructionStorage.add(consumer.get());
        return this;
    }

    @Override
    public @NonNull String getName() {
        return "model";
    }
}
