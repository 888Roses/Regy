package dev.rosenoire.regy.pipeline.datagen;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public record ProviderContext(AbstractRegy<?> getOwner, FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
}
