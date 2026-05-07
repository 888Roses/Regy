package dev.rosenoire.regy.pipeline.datagen.v2.impl.provider.model;

import dev.rosenoire.regy.pipeline.datagen.v2.DataGenProvider;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.world.item.Item;

public abstract class AbstractItemModelDataProvider implements DataGenProvider {
    public void simpleItem(ItemModelGenerators generators, Item item, ModelTemplate modelTemplate) {
        generators.generateFlatItem(item, modelTemplate);
    }

    public void simpleItem(ItemModelGenerators generators, Item item) {
        simpleItem(generators, item, ModelTemplates.FLAT_ITEM);
    }

    public void handheld(ItemModelGenerators generators, Item item, ItemModel.Unbaked guiModel, ItemModel.Unbaked handheldModel) {
        generators.itemModelOutput.accept(item, ItemModelGenerators.createFlatModelDispatch(guiModel, handheldModel));
    }
}
