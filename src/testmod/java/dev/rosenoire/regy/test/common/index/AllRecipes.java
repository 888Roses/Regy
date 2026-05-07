package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.pipeline.datagen.provider.recipe.RecipeInstruction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import static dev.rosenoire.regy.test.common.index.AllItems.*;

public interface AllRecipes {
    static void eldenNuggetsToEldenIngot(RecipeProvider provider, RecipeInstruction.RecipeContext ctx, RecipeOutput output) {
        provider.shapeless(RecipeCategory.MISC, ctx.itemEntry().get())
                .requires(ELDEN_NUGGET.get(), 9)
                .unlockedBy(RecipeProvider.getHasName(ctx.itemEntry().get()), provider.has(ctx.itemEntry().get()))
                .save(output);
    }

    static void eldenIngotToEldenNuggets(RecipeProvider provider, RecipeInstruction.RecipeContext ctx, RecipeOutput output) {
        provider.shapeless(RecipeCategory.MISC, ctx.itemEntry().get(), 9)
                .requires(ELDEN_INGOT.get())
                .unlockedBy(RecipeProvider.getHasName(ELDEN_INGOT.get()), provider.has(ELDEN_INGOT.get()))
                .save(output);
    }
}
