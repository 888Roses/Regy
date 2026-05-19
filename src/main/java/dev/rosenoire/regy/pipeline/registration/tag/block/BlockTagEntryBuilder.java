package dev.rosenoire.regy.pipeline.registration.tag.block;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.BlockTagDataGenerator;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.DataGenerators;
import dev.rosenoire.regy.pipeline.registration.tag.AbstractTagEntryBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

public class BlockTagEntryBuilder<P> extends AbstractTagEntryBuilder<P, Block, BlockTagEntry, BlockTagDataGenerator, BlockTagEntryBuilder<P>> {
    public BlockTagEntryBuilder(@NonNull AbstractRegy<?> regy, P parent, String identifier) {
        super(regy, parent, identifier, Registries.BLOCK, BlockTagEntry::new, DataGenerators.BLOCK_TAGS);
    }

    public BlockTagEntryBuilder<P> block(Block... blocks) {
        //noinspection unchecked
        return instruction(builder -> builder.add(blocks));
    }

    public BlockTagEntryBuilder<P> tag(TagKey<Block> blockTag) {
        //noinspection unchecked
        return instruction(builder -> builder.addTag(blockTag));
    }

    public BlockTagEntryBuilder<P> tag(BlockTagEntry entry) {
        return tag(entry.get());
    }

    public BlockTagEntryBuilder<P> optional(Block block) {
        //noinspection unchecked
        return instruction(builder -> builder.addOptional(block));
    }

    public BlockTagEntryBuilder<P> optionalTag(TagKey<Block> blockTag) {
        //noinspection unchecked
        return instruction(builder -> builder.addOptionalTag(blockTag));
    }

    public BlockTagEntryBuilder<P> optionalTag(BlockTagEntry entry) {
        return optionalTag(entry.get());
    }
}