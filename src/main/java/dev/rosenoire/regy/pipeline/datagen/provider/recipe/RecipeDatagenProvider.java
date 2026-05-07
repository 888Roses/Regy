package dev.rosenoire.regy.pipeline.datagen.provider.recipe;

import dev.rosenoire.regy.pipeline.datagen.ProviderContext;
import dev.rosenoire.regy.pipeline.datagen.ProviderType;
import dev.rosenoire.regy.pipeline.datagen.provider.DefaultDatagenProvider;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

public class RecipeDatagenProvider extends FabricRecipeProvider implements DefaultDatagenProvider {
    private final ProviderContext ctx;

    public RecipeDatagenProvider(ProviderContext ctx) {
        super(ctx.dataOutput(), ctx.registryLookup());
        this.ctx = ctx;
    }

    @Override
    protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider registryLookup, @NonNull RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);

                for (var entry : ctx.getOwner().datagenTargets.entrySet()) {
                    if (entry.getKey() instanceof DatagenRecipeTarget<?> recipeTarget && entry.getValue() instanceof ItemEntry<? extends Item> itemEntry) {
                        for (var recipe : recipeTarget.recipeStorage()) {
                            recipe.create(this, new RecipeInstruction.RecipeContext(ctx, recipeTarget, itemEntry, itemLookup), output);
                        }
                    }
                }
            }
        };
    }

    @Override
    public ProviderType getType() {
        return ProviderType.ITEM_TAG;
    }
}
