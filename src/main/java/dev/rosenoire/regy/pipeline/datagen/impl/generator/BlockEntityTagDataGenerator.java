package dev.rosenoire.regy.pipeline.datagen.impl.generator;

import dev.rosenoire.regy.pipeline.registration.tag.TagDefinition;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

public class BlockEntityTagDataGenerator extends FabricTagProvider.BlockEntityTypeTagProvider implements TagDataGenerator<BlockEntityType<?>, BlockEntityTagDataGenerator> {
    private final Map<TagKey<BlockEntityType<?>>, UnaryOperator<TagDefinition<BlockEntityType<?>, BlockEntityType<?>>>> tagStorage = new HashMap<>();

    public BlockEntityTagDataGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        this.tagStorage.forEach((tag, func) ->
                func.apply(transform(valueLookupBuilder(tag)))
        );
    }

    @Override
    public BlockEntityTagDataGenerator tag(TagKey<BlockEntityType<?>> tag, UnaryOperator<TagDefinition<BlockEntityType<?>, BlockEntityType<?>>> func) {
        this.tagStorage.put(tag, func);
        return this;
    }

    @Override
    public @NonNull String getName() {
        return DataGenerators.BLOCK_ENTITY_TAGS;
    }
}