package dev.rosenoire.regy.pipeline.datagen.impl.adapter;

import dev.rosenoire.regy.api.data.NonNullSupplier;
import dev.rosenoire.regy.pipeline.client.AbstractClientRegy;
import dev.rosenoire.regy.pipeline.client.ClientEntryBuilderAdapter;
import dev.rosenoire.regy.pipeline.client.registration.item.item.ClientItemEntryBuilder;
import dev.rosenoire.regy.pipeline.datagen.DataGenObject;
import dev.rosenoire.regy.pipeline.registration.Entry;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ItemAdapter implements ClientEntryBuilderAdapter {
    @Override
    public @Nullable NonNullSupplier<DataGenObject> getSupplier(@NonNull AbstractClientRegy<?, ?> client, @NonNull Entry<?> entry) {
        return ofType(ItemEntry.class, (c, e) -> new ClientItemEntryBuilder<>(c, e), client, entry);
    }
}