package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import static dev.rosenoire.regy.test.common.index.AllItems.*;

public interface AllRecipes {
    /*
    static void eldenIngotToEldenNuggets(DataGeneration dataGeneration, RecipeProvider provider, RecipeOutput output, ItemDataState<Item> data) {
        provider.shapeless(RecipeCategory.MISC, data.item(), 9)
                .requires(ELDEN_INGOT.get())
                .unlockedBy(RecipeProvider.getHasName(data.item()), provider.has(data.item()))
                .save(output);
    }

    static void eldenNuggetsToEldenIngot(DataGeneration dataGeneration, RecipeProvider provider, RecipeOutput output, ItemDataState<Item> data) {
        provider.shaped(RecipeCategory.MISC, data.item(), 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ELDEN_NUGGET.get())
                .unlockedBy(RecipeProvider.getHasName(ELDEN_INGOT.get()), provider.has(ELDEN_INGOT.get()))
                .save(output, data.name() + "_from_nuggets");
    }

    static void eldenIngotsToEldenBlock(DataGeneration dataGeneration, RecipeProvider provider, RecipeOutput output, ItemDataState<BlockItem> state) {
        provider.shaped(RecipeCategory.MISC, state.item())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', AllItems.ELDEN_INGOT.get())
                .unlockedBy(RecipeProvider.getHasName(AllItems.ELDEN_INGOT.get()), provider.has(AllItems.ELDEN_INGOT.get()))
                .save(output);
    }

    static void eldenBlockToEldenIngots(DataGeneration dataGeneration, RecipeProvider provider, RecipeOutput output, ItemDataState<BlockItem> state) {
        provider.shapeless(RecipeCategory.MISC, AllItems.ELDEN_INGOT.get(), 9)
                .requires(state.item())
                .unlockedBy(RecipeProvider.getHasName(AllItems.ELDEN_INGOT.get()), provider.has(AllItems.ELDEN_INGOT.get()))
                .save(output);
    }
     */
}
