package dev.rosenoire.regy.pipeline.client.registration;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.RegyOwnable;
import dev.rosenoire.regy.pipeline.datagen.DataGenObject;
import dev.rosenoire.regy.pipeline.registration.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

@Environment(EnvType.CLIENT)
public abstract class AbstractClientEntryBuilder<E extends Entry<V>, V> implements RegyOwnable, DataGenObject {
    private final @NonNull AbstractRegy<?> regy;
    private final @NonNull E entry;

    public AbstractClientEntryBuilder(@NonNull AbstractRegy<?> regy, @NonNull E entry) {
        this.regy = regy;
        this.entry = entry;
    }

    @Override
    public @NonNull AbstractRegy<?> regy() {
        return this.regy;
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
        this.regy().log.info("Adding data-generation object for '{}'...", this.entry().regyIdentifier());
        this.regy().dataGeneration().addData(this);
    }
}
