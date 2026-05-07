package dev.rosenoire.regy.pipeline.datagen.v1.provider.recipe;

import dev.rosenoire.regy.pipeline.datagen.v1.DatagenTarget;

import java.util.List;

public interface DatagenRecipeTarget<T extends DatagenRecipeTarget<T>> extends DatagenTarget {
    List<RecipeInstruction> recipeStorage();

    default T self() {
        //noinspection unchecked
        return (T) this;
    }

    default T recipe(RecipeInstruction instruction) {
        recipeStorage().add(instruction);
        return self();
    }
}