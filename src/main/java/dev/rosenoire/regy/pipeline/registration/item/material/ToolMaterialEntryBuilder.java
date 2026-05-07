package dev.rosenoire.regy.pipeline.registration.item.material;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.datagen.ProviderType;
import dev.rosenoire.regy.pipeline.datagen.provider.tag.DatagenTagTarget;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.BiConsumer;

@SuppressWarnings("UnusedReturnValue")
public class ToolMaterialEntryBuilder<P> extends AbstractEntryBuilder<ToolMaterialEntry, P> implements DatagenTagTarget {
    private @NonNull TagKey<Block> incorrectBlocksForDrops;

    public ToolMaterialEntryBuilder(@NonNull AbstractRegy<?> owner, P parent, String identifier) {
        super(owner, parent, identifier);
        incorrectBlocksForDrops();
    }

    @Override
    public @NonNull ToolMaterialEntry register() {
        return null;
    }

    // region modifiers

    public ToolMaterialEntryBuilder<P> incorrectBlocksForDrops(@NonNull TagKey<Block> tag) {
        this.incorrectBlocksForDrops = tag;
        return this;
    }

    public ToolMaterialEntryBuilder<P> incorrectBlocksForDrops() {
        this.incorrectBlocksForDrops = TagKey.create(Registries.BLOCK, identifier().withPath(path ->"incorrect_for_" + path + "_tool"));
        return this;
    }

    // endregion

    @Override
    public List<TagKey<?>> getAllTags() {
        return List.of();
    }

    @Override
    public void registerTagPostProcessors(BiConsumer<ProviderType, TagPostProcessor> consumer) {
        // TODO:
        // consumer.accept(ProviderType.BLOCK_TAG, this::modifyBlockTags);
    }
}
