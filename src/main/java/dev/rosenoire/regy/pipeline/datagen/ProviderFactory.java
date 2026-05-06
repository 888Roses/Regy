package dev.rosenoire.regy.pipeline.datagen;

import dev.rosenoire.regy.pipeline.datagen.provider.AbstractDatagenProvider;

@FunctionalInterface
public interface ProviderFactory {
    AbstractDatagenProvider bake(ProviderContext ctx);
}