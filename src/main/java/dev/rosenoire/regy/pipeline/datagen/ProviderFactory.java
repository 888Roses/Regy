package dev.rosenoire.regy.pipeline.datagen;

import dev.rosenoire.regy.pipeline.datagen.provider.RegyDatagenProvider;

@FunctionalInterface
public interface ProviderFactory {
    RegyDatagenProvider bake(ProviderContext ctx);
}