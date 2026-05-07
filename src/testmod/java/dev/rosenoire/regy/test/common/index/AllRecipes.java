package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntryBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;

import static dev.rosenoire.regy.test.common.index.AllItems.*;

public interface AllRecipes {
    static void eldenIngotToEldenNuggets(DataGeneration dataGeneration, RecipeProvider provider, RecipeOutput output, ItemEntryBuilder.DatagenData<Item> data) {
        provider.shapeless(RecipeCategory.MISC, data.item(), 9)
                .requires(ELDEN_NUGGET.get())
                .unlockedBy(RecipeProvider.getHasName(data.item()), provider.has(data.item()))
                .save(output);
    }

    static void eldenNuggetsToEldenIngot(DataGeneration dataGeneration, RecipeProvider provider, RecipeOutput output, ItemEntryBuilder.DatagenData<Item> data) {
        provider.shaped(RecipeCategory.MISC, data.item(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ELDEN_NUGGET.get())
                .unlockedBy(RecipeProvider.getHasName(ELDEN_INGOT.get()), provider.has(ELDEN_INGOT.get()))
                .save(output, data.name() + "_from_nuggets");
    }
}
