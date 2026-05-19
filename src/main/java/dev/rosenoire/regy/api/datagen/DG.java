package dev.rosenoire.regy.api.datagen;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.NonNull;

import static net.minecraft.client.data.models.model.ItemModelUtils.plainModel;

@SuppressWarnings("unused")
public interface DG {
    ModelTemplate DEFAULT_MODEL_TEMPLATE = ModelTemplates.FLAT_ITEM;
    String DEFAULT_IN_GUI_MODEL_SUFFIX = "";
    String DEFAULT_IN_WORLD_MODEL_SUFFIX = "_handheld";

    // region Location

    static @NonNull Identifier location(@NonNull Item item) {
        return BuiltInRegistries.ITEM.getKey(item).withPrefix("item/");
    }

    static @NonNull Identifier location(@NonNull Item item, @NonNull String suffix) {
        var path = BuiltInRegistries.ITEM.getKey(item);
        return path.withPath(base -> "item/" + base + suffix);
    }

    static @NonNull Identifier location(@NonNull Identifier root, @NonNull String suffix) {
        return root.withPath(base -> base + suffix);
    }

    // endregion

    // region Textures

    static @NonNull TextureMapping texture(@NonNull Item item) {
        return TextureMapping.layer0(item);
    }

    static @NonNull TextureMapping texture(@NonNull Item item, @NonNull String suffix) {
        return new TextureMapping().put(TextureSlot.LAYER0, location(item, suffix));
    }

    // endregion

    // region Unbaked

    static @NonNull Identifier createModel(
            @NonNull ItemModelGenerators generators,
            @NonNull ModelTemplate modelTemplate,
            @NonNull Identifier location,
            @NonNull TextureMapping texture
    ) {
        return modelTemplate.create(
                location,
                texture,
                generators.modelOutput
        );
    }

    static @NonNull Identifier createModel(
            @NonNull ItemModelGenerators generators,
            @NonNull ModelTemplate modelTemplate,
            @NonNull Item item,
            @NonNull TextureMapping texture
    ) {
        return createModel(generators, modelTemplate, location(item), texture);
    }

    static @NonNull Identifier createModel(
            @NonNull ItemModelGenerators generators,
            @NonNull ModelTemplate modelTemplate,
            @NonNull Item item,
            @NonNull TextureMapping texture,
            @NonNull String suffix
    ) {
        return createModel(generators, modelTemplate, location(item, suffix), texture);
    }

    static @NonNull Identifier createModel(
            @NonNull ItemModelGenerators generators,
            @NonNull Item item,
            @NonNull TextureMapping texture
    ) {
        return createModel(generators, DEFAULT_MODEL_TEMPLATE, item, texture);
    }

    static @NonNull Identifier createModel(
            @NonNull ItemModelGenerators generators,
            @NonNull Item item,
            @NonNull TextureMapping texture,
            @NonNull String suffix
    ) {
        return createModel(generators, DEFAULT_MODEL_TEMPLATE, item, texture, suffix);
    }

    // endregion

    // region Shortcuts

    static ItemModel.@NonNull Unbaked handheldItemModelDefinition(
            ItemModel.@NonNull Unbaked modelInGui,
            ItemModel.@NonNull Unbaked modelInWorld
    ) {
        return ItemModelGenerators.createFlatModelDispatch(modelInGui, modelInWorld);
    }

    static ItemModel.@NonNull Unbaked plainHandheldItemModelDefinition(
            @NonNull Identifier modelInGui,
            @NonNull Identifier modelInWorld
    ) {
        return handheldItemModelDefinition(
                plainModel(modelInGui),
                plainModel(modelInWorld)
        );
    }

    static ItemModel.@NonNull Unbaked plainHandheldItemModelDefinition(
            @NonNull Identifier root,
            @NonNull String modelInGuiSuffix,
            @NonNull String modelInWorldSuffix
    ) {
        return plainHandheldItemModelDefinition(
                location(root, modelInGuiSuffix),
                location(root, modelInWorldSuffix)
        );
    }

    static ItemModel.@NonNull Unbaked plainHandheldItemModelDefinition(@NonNull Identifier root) {
        return plainHandheldItemModelDefinition(
                root,
                DEFAULT_IN_GUI_MODEL_SUFFIX,
                DEFAULT_IN_WORLD_MODEL_SUFFIX
        );
    }

    // endregion

    // region Generating

    static void generate(
            @NonNull ItemModelGenerators generators,
            @NonNull ItemLike item,
            ItemModel.@NonNull Unbaked model,
            ClientItem.@NonNull Properties properties
    ) {
        generators.itemModelOutput.accept(
                item.asItem(),
                model,
                properties
        );
    }

    static void generate(
            @NonNull ItemModelGenerators generators,
            @NonNull ItemLike item,
            ItemModel.@NonNull Unbaked model
    ) {
        generators.itemModelOutput.accept(
                item.asItem(),
                model
        );
    }

    // endregion
}
