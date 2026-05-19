package dev.rosenoire.regy.pipeline.client.registration.item.item;

import dev.rosenoire.regy.api.datagen.DG;
import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

public @FunctionalInterface interface ModelInstruction<I extends Item> {
    void generateModel(@NonNull DataGeneration dataGeneration, @NonNull ItemModelGenerators generators, @NonNull ItemEntry<I> entry);

    static <I extends Item> @NonNull ModelInstruction<I> simple() {
        return (dataGeneration, generators, entry) -> {
            if (entry.get() instanceof BlockItem) {
                return;
            }

            generators.generateFlatItem(entry.get(), ModelTemplates.FLAT_ITEM);
        };
    }

    static <I extends Item> @NonNull ModelInstruction<I> handheld(
            ItemModel.@NonNull Unbaked modelInGui,
            ItemModel.@NonNull Unbaked modelInWorld
    ) {
        return (dataGeneration, generators, entry) -> DG
                .generate(
                        generators,
                        entry,
                        DG.handheldItemModelDefinition(
                                modelInGui,
                                modelInWorld
                        )
                );
    }

    static <I extends Item> @NonNull ModelInstruction<I> plainHandheld(
            @NonNull Identifier root,
            @NonNull String modelInGuiSuffix,
            @NonNull String modelInWorldSuffix
    ) {
        return (dataGeneration, generators, entry) -> DG
                .generate(
                        generators,
                        entry,
                        DG.plainHandheldItemModelDefinition(
                                root,
                                modelInGuiSuffix,
                                modelInWorldSuffix
                        )
                );
    }

    static <I extends Item> @NonNull ModelInstruction<I> plainHandheld(@NonNull Identifier root) {
        return (dataGeneration, generators, entry) -> DG
                .generate(
                        generators,
                        entry,
                        DG.plainHandheldItemModelDefinition(root)
                );
    }
}
