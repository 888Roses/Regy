package dev.rosenoire.regy.pipeline.client;

import dev.rosenoire.regy.api.event.WrappingCallback;
import dev.rosenoire.regy.api.event.WrappingValueEvent;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.RegyOwnable;
import dev.rosenoire.regy.pipeline.client.registration.block.ClientBlockEntryBuilder;
import dev.rosenoire.regy.pipeline.client.registration.item.potion.ClientPotionEntryBuilder;
import dev.rosenoire.regy.pipeline.client.registration.sound.ClientSoundEntryBuilder;
import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.registration.Entry;
import dev.rosenoire.regy.pipeline.registration.block.BlockEntry;
import dev.rosenoire.regy.pipeline.registration.block.BlockRenderMode;
import dev.rosenoire.regy.pipeline.client.registration.item.item.ClientItemEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import dev.rosenoire.regy.pipeline.registration.item.potion.PotionEntry;
import dev.rosenoire.regy.pipeline.registration.sound.SoundEntry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractClientRegy<R extends AbstractRegy<R>, SELF extends AbstractClientRegy<R, ?>> implements RegyOwnable {
    protected final @NonNull R instance;

    // region internal

    protected AbstractClientRegy(@NonNull R instance) {
        this.instance = instance;
    }

    @Override
    public @NonNull AbstractRegy<?> regy() {
        return instance;
    }

    public @NonNull SELF self() {
        //noinspection unchecked
        return (SELF) this;
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
        return new ClientItemEntryBuilder<>(this, itemEntry);
    }

    public <B extends Block> ClientBlockEntryBuilder<B> block(BlockEntry<B> blockEntry) {
        return new ClientBlockEntryBuilder<>(this, blockEntry);
    }

    public ClientPotionEntryBuilder potion(PotionEntry potionEntry) {
        return new ClientPotionEntryBuilder(this, potionEntry);
    }

    public ClientSoundEntryBuilder sound(SoundEntry soundEntry) {
        return new ClientSoundEntryBuilder(this, soundEntry);
    }

    // endregion

    // region data generation

    public final WrappingValueEvent<FabricDataGenerator.@NonNull Pack> onSetupDatagen = WrappingValueEvent.create();
    public final WrappingCallback onRunDatagen = WrappingCallback.create();

    protected boolean hasRanDatagen;
    protected FabricDataGenerator.Pack pack;
    protected DataGeneration dataGeneration = new DataGeneration(this);

    public DataGeneration dataGeneration() {
        return this.dataGeneration;
    }

    public void setupDatagen(@NonNull FabricDataGenerator dataGenerator) {
        this.setupDatagen(dataGenerator.createPack());
    }

    public void setupDatagen(FabricDataGenerator.@NonNull Pack pack) {
        if (this.hasRanDatagen) {
            this.regy().log.warn("Cannot run datagen multiple times!");
            return;
        }

        this.onSetupDatagen.before().accept(pack);
        this.setPack(pack);
        this.onSetupDatagen.after().accept(pack);
    }

    public FabricDataGenerator.@NonNull Pack getPack() {
        return Optional.ofNullable(this.pack).orElseThrow(() -> new IllegalStateException("Pack was null! This may happen when the pack is obtained after datagen has been run, or before the owner mod has setup datagen. If you're the owner mod, please call your client REGY instance's setupDatagen() in your DataGeneratorEntrypoint, supplying the fabricDataGenerator."));
    }

    public void setPack(FabricDataGenerator.@Nullable Pack pack) {
        this.pack = pack;
    }

    public void runDatagen() {
        this.onRunDatagen.before().run();
        this.hasRanDatagen = true;
        this.onRunDatagen.after().run();
        this.setPack(null);
    }

    // endregion
}
