package dev.rosenoire.regy.pipeline.client.registration.item.model.properties;

import com.mojang.serialization.MapCodec;
import dev.rosenoire.regy.api.logging.LogEntry;
import dev.rosenoire.regy.pipeline.client.AbstractClientRegy;
import dev.rosenoire.regy.pipeline.client.ClientRegyOwnable;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public abstract class AbstractItemModelPropertyBuilder<P, E, V, S extends AbstractItemModelPropertyBuilder<P, E, V, S>> implements ClientRegyOwnable {
    protected final @NonNull AbstractClientRegy<?, ?> client;
    protected final Identifier identifier;
    protected V key;

    public AbstractItemModelPropertyBuilder(@NonNull AbstractClientRegy<?, ?> client, String identifier) {
        this.client = client;
        this.identifier = this.client().regy().id(identifier);
    }

    @Override
    public @NonNull AbstractClientRegy<?, ?> client() {
        return this.client;
    }

    protected S self() {
        //noinspection unchecked
        return (S) this;
    }

    public S key(@NonNull V key) {
        this.key = key;
        return this.self();
    }

    public E register() {
        var log = LogEntry.of(this);

        log.info("|> §bold§yellow({})§end §green\"{}\"§end", this.getClass().getSimpleName(), this.identifier);

        if (this.key != null) {
            log.info("§white|§end  > Registering in ID Mapper...");
            this.registerProperty(this.identifier, this.key);
            // ConditionalItemModelProperties.ID_MAPPER.put(this.identifier, this.codec);
        } else {
            log.error("§white|§end  > Could not register in ID Mapper!");
            log.error("§white|§end    ↪ The codec of this {} wasn't assigned!", this.getClass().getSimpleName());
        }

        log.info("§white|§end  > Creating Properties...");
        var properties = createEntry(this.identifier);

        log.send();

        return properties;
    }

    protected abstract void registerProperty(Identifier identifier, V codec);
    protected abstract E createEntry(Identifier identifier);
}
