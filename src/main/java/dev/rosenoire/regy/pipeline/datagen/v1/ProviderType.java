package dev.rosenoire.regy.pipeline.datagen.v1;

import dev.rosenoire.regy.pipeline.datagen.v1.provider.lang.LangDatagenProvider;
import dev.rosenoire.regy.pipeline.datagen.v1.provider.model.ModelDatagenProvider;
import dev.rosenoire.regy.pipeline.datagen.v1.provider.recipe.RecipeDatagenProvider;
import dev.rosenoire.regy.pipeline.datagen.v1.provider.tag.ItemTagDatagenProvider;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

public enum ProviderType implements StringRepresentable {
    LANG("lang", LangDatagenProvider::new),
    MODEL("item_model", ModelDatagenProvider::new),
    ITEM_TAG("item_tag", ItemTagDatagenProvider::new),
    // BLOCK_TAG("block_tag", BlockTagDatagenProvider::new),
    RECIPE("recipe", RecipeDatagenProvider::new),

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

