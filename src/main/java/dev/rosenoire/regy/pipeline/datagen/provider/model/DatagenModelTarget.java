package dev.rosenoire.regy.pipeline.datagen.provider.model;

import dev.rosenoire.regy.pipeline.datagen.DatagenTarget;
import dev.rosenoire.regy.pipeline.datagen.ProviderContext;
import dev.rosenoire.regy.pipeline.registration.AbstractRegistryEntry;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

@ApiStatus.NonExtendable
public interface DatagenModelTarget extends DatagenTarget {
    void internal$generate(ProviderContext ctx, AbstractRegistryEntry<?, ?> entry, @Nullable ItemModelGenerators itemModelGenerators, @Nullable BlockModelGenerators blockModelGenerators);

    interface ItemTarget extends DatagenModelTarget {
        @Override
        @ApiStatus.NonExtendable
        @ApiStatus.Internal
        default void internal$generate(ProviderContext ctx, AbstractRegistryEntry<?, ?> abstractEntry, @Nullable ItemModelGenerators gen, @Nullable BlockModelGenerators ignored) {
            assert gen != null;

            if (!(abstractEntry instanceof ItemEntry<? extends Item> entry)) {
                throw new IllegalStateException("Tried generating datagen item model for non-item AbstractEntry '" + abstractEntry.getClass().getSimpleName() + "'!");
            }

            getInstruction().generate(ctx, entry, gen);
        }

        ItemModelInstruction getInstruction();
    }
}

