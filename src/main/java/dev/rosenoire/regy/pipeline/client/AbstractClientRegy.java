package dev.rosenoire.regy.pipeline.client;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.RegyOwnable;
import dev.rosenoire.regy.pipeline.client.registration.block.ClientBlockEntryBuilder;
import dev.rosenoire.regy.pipeline.client.registration.item.potion.ClientPotionEntryBuilder;
import dev.rosenoire.regy.pipeline.client.registration.sound.ClientSoundEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.Entry;
import dev.rosenoire.regy.pipeline.registration.block.BlockEntry;
import dev.rosenoire.regy.pipeline.registration.block.BlockRenderMode;
import dev.rosenoire.regy.pipeline.client.registration.item.item.ClientItemEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import dev.rosenoire.regy.pipeline.registration.item.potion.PotionEntry;
import dev.rosenoire.regy.pipeline.registration.sound.SoundEntry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

public abstract class AbstractClientRegy<R extends AbstractRegy<R>> implements RegyOwnable {
    protected final @NonNull R instance;

    // region internal

    protected AbstractClientRegy(@NonNull R instance) {
        this.instance = instance;
    }

    @Override
    public @NonNull AbstractRegy<?> regy() {
        return instance;
    }

    // endregion

    // region events

    public void initializeEvents() {
        this.regy().log.info("Initializing client events...");

        registerAllBlockEntryRenderModes();
    }

    private void registerAllBlockEntryRenderModes() {
        this.regy().log.info("|-- Register block entries RenderMode...");
        for (Entry<?> entry : this.instance.entries()) {
            if (entry instanceof BlockEntry<?> blockEntry) {
                registerBlockEntryRenderMode(blockEntry);
            }
        }
    }

    private void registerBlockEntryRenderMode(BlockEntry<?> blockEntry) {
        var identifier = blockEntry.identifier();
        this.regy().log.info("|---- Checking BlockEntry '{}' RenderMode...", identifier);

        var renderMode = blockEntry.renderMode;
        if (renderMode != BlockRenderMode.SOLID) {
            BlockRenderLayerMap.putBlock(blockEntry.get(), switch (renderMode) {
                case CUTOUT -> ChunkSectionLayer.CUTOUT;
                case TRANSLUCENT -> ChunkSectionLayer.TRANSLUCENT;
                case TRIPWIRE -> ChunkSectionLayer.TRIPWIRE;
                case null -> ChunkSectionLayer.SOLID;
                default -> {
                    throw new IllegalStateException("Could not register BlockEntry " + identifier + " in RenderMode: " + renderMode);
                }
            });
        }
    }

    // endregion

    // region registration

    public <I extends Item> ClientItemEntryBuilder<I> item(ItemEntry<I> itemEntry) {
        return new ClientItemEntryBuilder<>(this.regy(), itemEntry);
    }

    public <B extends Block> ClientBlockEntryBuilder<B> block(BlockEntry<B> blockEntry) {
        return new ClientBlockEntryBuilder<>(this.regy(), blockEntry);
    }

    public ClientPotionEntryBuilder potion(PotionEntry potionEntry) {
        return new ClientPotionEntryBuilder(this.regy(), potionEntry);
    }

    public ClientSoundEntryBuilder sound(SoundEntry soundEntry) {
        return new ClientSoundEntryBuilder(this.regy(), soundEntry);
    }

    // endregion
}
