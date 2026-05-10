package dev.rosenoire.regy.pipeline.content;

import dev.rosenoire.regy.pipeline.registration.block.BlockEntryBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.UnaryOperator;

@ApiStatus.NonExtendable
public interface BlockTransformers {
    static <B extends Block, P> UnaryOperator<BlockEntryBuilder<B, P>> nonFullBlock() {
        return builder -> builder.properties(properties -> properties
                .noOcclusion()
                .isValidSpawn(Blocks::never)
                .isRedstoneConductor(Blocks::never)
        );
    }

    @SafeVarargs
    static <B extends Block, P> UnaryOperator<BlockEntryBuilder<B, P>> taggedItem(TagKey<Item>... tags) {
        return builder -> {
            var itemBuilder = builder.item();
            for (var tag : tags) itemBuilder.tag(tag);
            return itemBuilder.build();
        };
    }

    static UnaryOperator<BlockBehaviour.Properties> requiresCorrectToolForDrops() {
        return BlockBehaviour.Properties::requiresCorrectToolForDrops;
    }
}
