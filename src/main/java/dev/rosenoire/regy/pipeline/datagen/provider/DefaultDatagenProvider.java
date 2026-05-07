package dev.rosenoire.regy.pipeline.datagen.provider;

import dev.rosenoire.regy.api.data.NonNullType;
import dev.rosenoire.regy.pipeline.datagen.ProviderType;
import org.jspecify.annotations.NonNull;

public interface DefaultDatagenProvider extends RegyDatagenProvider {
    @NonNullType
    ProviderType getType();

    @Override
    default @NonNull String getName() {
        return getType().getSerializedName();
    }
}
