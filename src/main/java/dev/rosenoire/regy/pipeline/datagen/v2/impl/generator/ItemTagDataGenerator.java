package dev.rosenoire.regy.pipeline.datagen.v2.impl.generator;

import dev.rosenoire.regy.pipeline.datagen.v2.DataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class ItemTagDataGenerator extends FabricTagProvider.ItemTagProvider implements DataGenerator {
    private final Map<TagKey<Item>, UnaryOperator<TagAppender<Item, Item>>> tagStorage = new HashMap<>();

    public ItemTagDataGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        this.tagStorage.forEach((tag, func) -> func.apply(valueLookupBuilder(tag)));
    }

    public ItemTagDataGenerator tag(TagKey<Item> tag, UnaryOperator<TagAppender<Item, Item>> func) {
        this.tagStorage.put(tag, func);
        return this;
    }

    @Override
    public @NonNull String getName() {
        return "item_tag";
    }
}