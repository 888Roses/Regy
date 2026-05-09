package dev.rosenoire.regy.pipeline.registration.sound;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

public @FunctionalInterface interface SoundInstruction {
    void export(
            HolderLookup.@NonNull Provider registryLookup,
            FabricSoundsProvider.@NonNull SoundExporter exporter
    );
}