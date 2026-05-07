package dev.rosenoire.regy.pipeline.datagen.provider.model;

import com.mojang.datafixers.util.Either;
import dev.rosenoire.regy.pipeline.datagen.ProviderContext;
import dev.rosenoire.regy.pipeline.datagen.ProviderType;
import dev.rosenoire.regy.pipeline.datagen.provider.DefaultDatagenProvider;
import dev.rosenoire.regy.pipeline.registration.AbstractRegistryEntry;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import org.jspecify.annotations.NonNull;

public class ModelDatagenProvider extends FabricModelProvider implements DefaultDatagenProvider {
    private final ProviderContext ctx;

    public ModelDatagenProvider(ProviderContext providerContext) {
        super(providerContext.dataOutput());
        this.ctx = providerContext;
    }

    @Override
    public ProviderType getType() {
        return ProviderType.MODEL;
    }

    @Override
    public void generateBlockStateModels(@NonNull BlockModelGenerators blockStateModelGenerator) {
        generateEitherModels(Either.right(blockStateModelGenerator));
    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerators itemModelGenerator) {
        generateEitherModels(Either.left(itemModelGenerator));
    }

    private void generateEitherModels(Either<ItemModelGenerators, BlockModelGenerators> generators) {
        for (var entry : ctx.getOwner().datagenTargets.entrySet()) {
            if (entry.getKey() instanceof DatagenModelTarget.ItemTarget modelTarget && entry.getValue() instanceof AbstractRegistryEntry<?, ?> abstractEntry) {
                generators.ifLeft(left -> modelTarget.internal$generate(ctx, abstractEntry, left, null));
            }
        }
    }
}
