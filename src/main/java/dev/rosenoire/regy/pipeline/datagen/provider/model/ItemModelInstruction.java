package dev.rosenoire.regy.pipeline.datagen.provider.model;

import dev.rosenoire.regy.pipeline.datagen.ProviderContext;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.renderer.item.properties.conditional.IsUsingItem;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ItemModelInstruction {
    ItemModelInstruction SIMPLE = (ctx, entry, gen) -> gen.generateFlatItem(entry.get(), ModelTemplates.FLAT_ITEM);
    BiFunction<ItemModel.Unbaked, ItemModel.Unbaked, ItemModelInstruction> HANDHELD_MODEL = (gui, handheld) ->
            (ctx, itemEntry, generator) ->
                    generator.itemModelOutput.accept(itemEntry.get(), ItemModelGenerators.createFlatModelDispatch(gui, handheld));

    void generate(ProviderContext ctx, ItemEntry<? extends Item> itemEntry, @NonNull ItemModelGenerators generator);
}