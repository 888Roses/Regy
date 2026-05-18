package dev.rosenoire.regy.pipeline.client.registration.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

@Environment(EnvType.CLIENT)
public @FunctionalInterface interface SoundInstruction {
    void export(
            HolderLookup.@NonNull Provider registryLookup,
            FabricSoundsProvider.@NonNull SoundExporter exporter
    );
}