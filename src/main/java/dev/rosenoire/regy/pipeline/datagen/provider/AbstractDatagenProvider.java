package dev.rosenoire.regy.pipeline.datagen.provider;

import dev.rosenoire.regy.pipeline.datagen.ProviderContext;
import net.minecraft.data.DataProvider;

public abstract class AbstractDatagenProvider implements DataProvider {
    protected final ProviderContext ctx;

    public AbstractDatagenProvider(ProviderContext providerContext) {
        this.ctx = providerContext;
    }
}

