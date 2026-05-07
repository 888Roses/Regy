package dev.rosenoire.regy.pipeline.datagen.impl.generator;

import dev.rosenoire.regy.pipeline.datagen.DataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecipeDataGenerator extends FabricRecipeProvider implements DataGenerator {
    private final List<RecipeConsumer> recipeStorage = new ArrayList<>();

    public RecipeDataGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider registryLookup, @NonNull RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                synchronized (recipeStorage) {
                    recipeStorage.forEach(recipe -> recipe.bakeAndServeRecipe(this, output));
                }
            }
        };
    }

    public void registerRecipe(@NonNull RecipeConsumer recipe) {
        this.recipeStorage.add(recipe);
    }

    @Override
    public @NonNull String getName() {
        return DataGenerators.RECIPES;
    }

    @FunctionalInterface
    public interface RecipeConsumer {
        void bakeAndServeRecipe(RecipeProvider provider, RecipeOutput output); // Dinner is serve~
    }
}
