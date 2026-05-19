package dev.rosenoire.regy.pipeline.datagen.impl.adapter;

import dev.rosenoire.regy.api.data.NonNullSupplier;
import dev.rosenoire.regy.pipeline.client.AbstractClientRegy;
import dev.rosenoire.regy.pipeline.client.ClientEntryBuilderAdapter;
import dev.rosenoire.regy.pipeline.client.registration.sound.ClientSoundEntryBuilder;
import dev.rosenoire.regy.pipeline.datagen.DataGenObject;
import dev.rosenoire.regy.pipeline.registration.Entry;
import dev.rosenoire.regy.pipeline.registration.sound.SoundEntry;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class SoundAdapter implements ClientEntryBuilderAdapter {
    @Override
    public @Nullable NonNullSupplier<DataGenObject> getSupplier(@NonNull AbstractClientRegy<?, ?> client, @NonNull Entry<?> entry) {
        return ofType(SoundEntry.class, ClientSoundEntryBuilder::new, client, entry);
    }
}
