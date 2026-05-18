package dev.rosenoire.regy.pipeline.client.registration;

import dev.rosenoire.regy.api.logging.LogEntry;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.client.AbstractClientRegy;
import dev.rosenoire.regy.pipeline.client.ClientRegyOwnable;
import dev.rosenoire.regy.pipeline.datagen.DataGenObject;
import dev.rosenoire.regy.pipeline.registration.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

@Environment(EnvType.CLIENT)
public abstract class AbstractClientEntryBuilder<E extends Entry<V>, V> implements ClientRegyOwnable, DataGenObject {
    private final @NonNull AbstractClientRegy<?, ?> client;
    private final @NonNull E entry;

    public AbstractClientEntryBuilder(@NonNull AbstractClientRegy<?, ?> client, @NonNull E entry) {
        this.client = client;
        this.entry = entry;
    }

    @Override
    public @NonNull AbstractClientRegy<?, ?> client() {
        return this.client;
    }

    public @NonNull E entry() {
        return this.entry;
    }

    public @NonNull V value() {
        return this.entry().get();
    }

    protected @NonNull Identifier identifier() {
        return this.entry().identifier();
    }

    protected @NonNull String path() {
        return this.identifier().getPath();
    }

    public void register() {
        this.client().dataGeneration().addData(this);

        LogEntry.of(this)
                .info("|> §bold§cyan({})§end §green\"{}\"§end", this.getClass().getSimpleName(), this.entry().regyIdentifier())
                .send();
    }
}
