package dev.rosenoire.regy.pipeline.datagen.impl.generator;

import dev.rosenoire.regy.pipeline.datagen.DataGenerator;
import dev.rosenoire.regy.pipeline.client.registration.sound.SoundInstruction;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SoundDataGenerator extends FabricSoundsProvider implements DataGenerator {
    private final List<SoundInstruction> instructionStorage = new ArrayList<>();

    public SoundDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.@NonNull Provider registryLookup, @NonNull SoundExporter exporter) {
        synchronized (this.instructionStorage) {
            this.instructionStorage.forEach(instruction -> instruction.export(registryLookup, exporter));
        }
    }

    public void add(@NonNull SoundInstruction instruction) {
        this.instructionStorage.add(instruction);
    }

    @Override
    public @NonNull String getName() {
        return DataGenerators.SOUNDS;
    }
}