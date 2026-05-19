package dev.rosenoire.regy.pipeline.client;

import dev.rosenoire.regy.api.data.NonNullSupplier;
import dev.rosenoire.regy.pipeline.client.registration.AbstractClientEntryBuilder;
import dev.rosenoire.regy.pipeline.datagen.DataGenObject;
import dev.rosenoire.regy.pipeline.registration.Entry;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.BiFunction;

public @FunctionalInterface interface ClientEntryBuilderAdapter {
    @Nullable NonNullSupplier<DataGenObject> getSupplier(@NonNull AbstractClientRegy<?, ?> client, @NonNull Entry<?> entry);

    default <E extends Entry<?>, B extends AbstractClientEntryBuilder<E, ?>> @Nullable NonNullSupplier<DataGenObject> ofType(
            Class<E> entryClass,
            BiFunction<@NonNull AbstractClientRegy<?, ?>, @NonNull E, @NonNull B> factory,
            @NonNull AbstractClientRegy<?, ?> client,
            @NonNull Entry<?> entry
    ) {
        if (entry.getClass().isAssignableFrom(entryClass)) {
            //noinspection unchecked
            return () -> factory.apply(client, (E) entry).register();
        }

        return null;
    }
}
