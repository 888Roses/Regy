package dev.rosenoire.regy.pipeline.client.registration.item.item;

import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public @FunctionalInterface interface ModelInstruction<I extends Item> {
    void generateModel(DataGeneration dataGen, ItemModelGenerators generators, ItemEntry<I> itemEntry);

    static <I extends Item> ModelInstruction<I> simple() {
        return (dataGen, generators, itemEntry) -> {
            if (itemEntry.get() instanceof BlockItem blockItem) {
                return;
            }

            generators.generateFlatItem(itemEntry.get(), ModelTemplates.FLAT_ITEM);
        };
    }

    static <I extends Item> ModelInstruction<I> handheld(net.minecraft.client.renderer.item.ItemModel.Unbaked gui, net.minecraft.client.renderer.item.ItemModel.Unbaked handheld) {
        return (dataGen, generators, itemEntry) -> generators.itemModelOutput.accept(itemEntry.get(), ItemModelGenerators.createFlatModelDispatch(gui, handheld));
    }
}
