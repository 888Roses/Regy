package dev.rosenoire.regy.pipeline.client.registration.item.item;

import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;

public @FunctionalInterface interface RecipeInstruction<I extends Item> {
    void generateRecipe(DataGeneration dataGeneration, RecipeProvider provider, RecipeOutput output, ItemEntry<I> itemEntry);
}
