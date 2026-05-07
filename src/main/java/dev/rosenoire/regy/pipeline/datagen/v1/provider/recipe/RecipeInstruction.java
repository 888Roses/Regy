package dev.rosenoire.regy.pipeline.datagen.v1.provider.recipe;

import dev.rosenoire.regy.pipeline.datagen.v1.ProviderContext;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;

@FunctionalInterface
public interface RecipeInstruction {
    void create(RecipeProvider recipeProvider, RecipeContext ctx, RecipeOutput output);

    record RecipeContext(
            ProviderContext providerContext,
            DatagenRecipeTarget<?> recipeTarget,
            ItemEntry<? extends Item> itemEntry,
            HolderLookup.RegistryLookup<Item> itemLookup
    ) {
    }
}
