package dev.rosenoire.regy.pipeline.datagen.impl.adapter;

import dev.rosenoire.regy.api.data.NonNullSupplier;
import dev.rosenoire.regy.pipeline.client.AbstractClientRegy;
import dev.rosenoire.regy.pipeline.client.ClientEntryBuilderAdapter;
import dev.rosenoire.regy.pipeline.client.registration.block.ClientBlockEntryBuilder;
import dev.rosenoire.regy.pipeline.datagen.DataGenObject;
import dev.rosenoire.regy.pipeline.registration.Entry;
import dev.rosenoire.regy.pipeline.registration.block.BlockEntry;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@SuppressWarnings({"unchecked", "rawtypes"})
public class BlockAdapter implements ClientEntryBuilderAdapter {
    @Override
    public @Nullable NonNullSupplier<DataGenObject> getSupplier(@NonNull AbstractClientRegy<?, ?> client, @NonNull Entry<?> entry) {
        return ofType(BlockEntry.class, (c, e) -> new ClientBlockEntryBuilder<>(c, e), client, entry);
    }
}