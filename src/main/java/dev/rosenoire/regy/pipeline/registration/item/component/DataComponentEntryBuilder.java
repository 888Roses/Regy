package dev.rosenoire.regy.pipeline.registration.item.component;

import com.mojang.serialization.Codec;
import dev.rosenoire.regy.api.logging.LogEntry;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class DataComponentEntryBuilder<V, P> extends AbstractEntryBuilder<DataComponentEntry<V>, P> {
    protected boolean cacheEncoding;
    protected boolean ignoreSwapAnimation;
    protected Codec<V> codec;
    protected StreamCodec<? super RegistryFriendlyByteBuf, V> networkCodec;

    public DataComponentEntryBuilder(@NonNull AbstractRegy<?> regy, P parent, String identifier) {
        super(regy, parent, identifier);
    }

    @Override
    public @NonNull DataComponentEntry<V> register() {
        var log = LogEntry.of(this);

        log.info("|> §bold§cyan(DataComponentEntry)§end §green\"{}\"§end", this.identifier());

        log.info("|  > Creating DataComponentType Builder...");
        var builder = DataComponentType.<V>builder();

        if (codec != null) {
            log.info("|  > Assigning Persistent Codec...");
            builder.persistent(codec);
        }

        if (networkCodec != null) {
            log.info("|  > Assigning Network Synchronized Codec...");
            builder.networkSynchronized(networkCodec);
        }

        if (ignoreSwapAnimation) {
            log.info("|  > Disabling swapping animation...");
            builder.ignoreSwapAnimation();
        }

        if (cacheEncoding) {
            log.info("|  > Caching Encoding...");
            builder.cacheEncoding();
        }

        log.info("|  > Creating DataComponentType...");
        var dataComponentType = builder.build();

        log.info("|  > Registering DataComponentType...");
        Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                this.identifier(),
                dataComponentType
        );

        log.info("|  > Creating Entry...");
        var componentEntry = regy().entry(new DataComponentEntry<>(
                dataComponentType,
                this.regyIdentifier(),
                this.identifier()
        ));

        log.send();

        return componentEntry;
    }

    @Override
    protected Identifier regyIdentifier() {
        return identifier().withPrefix("data_component/");
    }

    public DataComponentEntryBuilder<V, P> cacheEncoding() {
        this.cacheEncoding = true;
        return this;
    }

    public DataComponentEntryBuilder<V, P> ignoreSwapAnimation() {
        this.ignoreSwapAnimation = true;
        return this;
    }

    public DataComponentEntryBuilder<V, P> codec(Codec<V> codec) {
        this.codec = codec;
        return this;
    }

    public DataComponentEntryBuilder<V, P> streamCodec(StreamCodec<? super RegistryFriendlyByteBuf, V> networkCodec) {
        this.networkCodec = networkCodec;
        return this;
    }
}
