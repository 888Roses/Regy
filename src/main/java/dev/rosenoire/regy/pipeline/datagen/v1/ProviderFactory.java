package dev.rosenoire.regy.pipeline.datagen.v1;

import dev.rosenoire.regy.pipeline.datagen.v1.provider.RegyDatagenProvider;

@FunctionalInterface
public interface ProviderFactory {
    RegyDatagenProvider bake(ProviderContext ctx);
}