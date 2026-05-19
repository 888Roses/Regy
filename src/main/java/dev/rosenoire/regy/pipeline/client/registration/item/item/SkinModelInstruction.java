package dev.rosenoire.regy.pipeline.client.registration.item.item;

import dev.rosenoire.regy.api.datagen.DG;
import dev.rosenoire.regy.client.model.item.properties.select.ItemSkinProperty;
import dev.rosenoire.regy.common.content.datacomponent.ItemSkin;
import dev.rosenoire.regy.common.index.InternalDataComponents;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

import static dev.rosenoire.regy.api.datagen.DG.*;
import static net.minecraft.client.data.models.model.ItemModelUtils.*;

public @FunctionalInterface interface SkinModelInstruction<I extends Item> {
    ItemModel.@NonNull Unbaked getSkinModel(
            @NonNull String skinName,
            @NonNull Item item,
            @NonNull ItemSkin component,
            @NonNull ItemModelGenerators generators
    );

    default void collectSwitchCases(
            @NonNull List<SelectItemModel.SwitchCase<String>> cases,
            @NonNull Item item,
            @NonNull ItemSkin component,
            @NonNull ItemModelGenerators generators
    ) {
        for (var skinName : component.skins()) {
            var model = getSkinModel(skinName, item, component, generators);
            cases.add(new SelectItemModel.SwitchCase<>(List.of(skinName), model));
        }
    }

    default @NonNull ModelInstruction<I> compute() {
        return (dataGeneration, generators, itemEntry) -> {
            var item = itemEntry.get();
            var component = InternalDataComponents.ITEM_SKIN.get(item);

            if (component == null) {
                return;
            }

            var cases = new ArrayList<SelectItemModel.SwitchCase<String>>();
            this.collectSwitchCases(cases, item, component, generators);

            generators.itemModelOutput.accept(item, select(new ItemSkinProperty(), cases));
        };
    }

    static @NonNull TextureMapping getStandardMapping(@NonNull Item item, @NonNull String skinName) {
        return texture(item, "/" + skinName);
    }

    static @NonNull Identifier createSimpleModel(
            @NonNull Item item,
            @NonNull String skinName,
            @NonNull ModelTemplate modelTemplate,
            @NonNull ItemModelGenerators generators
    ) {
        return createModel(generators, modelTemplate, item, getStandardMapping(item, skinName), "_" + skinName);
    }

    static <I extends Item> @NonNull SkinModelInstruction<I> simple(@NonNull ModelTemplate modelTemplate) {
        return (skinName, item, component, generators) ->
                plainModel(createSimpleModel(item, skinName, modelTemplate, generators));
    }

    static <I extends Item> @NonNull SkinModelInstruction<I> simple() {
        return simple(ModelTemplates.FLAT_ITEM);
    }

    static <I extends Item> @NonNull SkinModelInstruction<I> handheld(
            ItemModel.@NonNull Unbaked modelInGui,
            ItemModel.@NonNull Unbaked modelInWorld
    ) {
        return (skinName, item, component, generators) -> DG
                .handheldItemModelDefinition(
                        modelInGui,
                        modelInWorld
                );
    }

    static <I extends Item> @NonNull SkinModelInstruction<I> plainHandheld(
            @NonNull Identifier modelInGui,
            @NonNull Identifier modelInWorld
    ) {
        return (skinName, item, component, generators) -> DG
                .plainHandheldItemModelDefinition(
                        modelInGui,
                        modelInWorld
                );
    }

    static <I extends Item> @NonNull SkinModelInstruction<I> plainHandheld(
            @NonNull Identifier root,
            @NonNull String modelInGuiSuffix,
            @NonNull String modelInWorldSuffix
    ) {
        return (skinName, item, component, generators) -> DG
                .plainHandheldItemModelDefinition(
                        root,
                        modelInGuiSuffix,
                        modelInWorldSuffix
                );
    }

    static <I extends Item> @NonNull SkinModelInstruction<I> plainHandheld(@NonNull Identifier root) {
        return (skinName, item, component, generators) -> DG
                .plainHandheldItemModelDefinition(root);
    }

    static <I extends Item> @NonNull SkinModelInstruction<I> plainHandheld() {
        return (skinName, item, component, generators) -> DG
                .plainHandheldItemModelDefinition(DG.location(item));
    }
}