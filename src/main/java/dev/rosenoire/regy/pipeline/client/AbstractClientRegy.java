package dev.rosenoire.regy.pipeline.client;

import dev.rosenoire.regy.common.RegyCommon;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.RegyOwnable;
import dev.rosenoire.regy.pipeline.registration.Entry;
import dev.rosenoire.regy.pipeline.registration.block.BlockEntry;
import dev.rosenoire.regy.pipeline.registration.block.BlockRenderMode;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import org.jspecify.annotations.NonNull;

public abstract class AbstractClientRegy<R extends AbstractRegy<R>> implements RegyOwnable {
    protected final @NonNull R instance;

    // region internal

    protected AbstractClientRegy(@NonNull R instance) {
        this.instance = instance;
    }

    @Override
    public @NonNull AbstractRegy<?> getRegy() {
        return instance;
    }

    // endregion

    // region events

    public void initializeEvents() {
        RegyCommon.log.info("Initializing Events!");

        for (Entry<?> entry : this.instance.entries()) {
            RegyCommon.log.info("for each entry: {} -> {}", entry.regyIdentifier(), entry.getClass().getSimpleName());
            if (entry instanceof BlockEntry<?> blockEntry) {
                RegyCommon.log.info("  for each block entry: {}", blockEntry.regyIdentifier());
                addBlockEntryInRenderLayerMap(blockEntry);
            }
        }
    }

    private static void addBlockEntryInRenderLayerMap(BlockEntry<?> blockEntry) {
        RegyCommon.log.info("'Hello??? {}", blockEntry.regyIdentifier());

        var renderMode = blockEntry.renderMode;
        if (renderMode != BlockRenderMode.SOLID) {
            BlockRenderLayerMap.putBlock(blockEntry.get(), switch (renderMode) {
                case CUTOUT -> ChunkSectionLayer.CUTOUT;
                case TRANSLUCENT -> ChunkSectionLayer.TRANSLUCENT;
                case TRIPWIRE -> ChunkSectionLayer.TRIPWIRE;
                case null -> ChunkSectionLayer.SOLID;
                default -> throw new IllegalStateException("Unexpected value: " + renderMode);
            });
        }
    }

    // endregion
}
