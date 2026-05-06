package dev.rosenoire.regy.pipeline.datagen;

import dev.rosenoire.regy.pipeline.datagen.provider.LangDatagenProvider;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

public enum ProviderType implements StringRepresentable {
    LANG("lang", LangDatagenProvider::new),

    ;

    private final String name;
    private final ProviderFactory providerFactory;

    ProviderType(@NonNull String name, ProviderFactory providerFactory) {
        this.name = name;
        this.providerFactory = providerFactory;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }

    public ProviderFactory getProviderFactory() {
        return providerFactory;
    }
}

