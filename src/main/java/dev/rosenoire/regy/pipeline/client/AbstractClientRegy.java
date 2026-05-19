package dev.rosenoire.regy.pipeline.client;

import dev.rosenoire.regy.api.data.NonNullSupplier;
import dev.rosenoire.regy.api.event.WrappingCallback;
import dev.rosenoire.regy.api.event.WrappingValueEvent;
import dev.rosenoire.regy.api.logging.LogEntry;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.RegyOwnable;
import dev.rosenoire.regy.pipeline.client.registration.AbstractClientEntryBuilder;
import dev.rosenoire.regy.pipeline.client.registration.block.ClientBlockEntryBuilder;
import dev.rosenoire.regy.pipeline.client.registration.item.model.properties.conditional.ConditionalItemModelPropertyBuilder;
import dev.rosenoire.regy.pipeline.client.registration.item.model.properties.select.SelectItemModelPropertyBuilder;
import dev.rosenoire.regy.pipeline.client.registration.item.potion.ClientPotionEntryBuilder;
import dev.rosenoire.regy.pipeline.client.registration.sound.ClientSoundEntryBuilder;
import dev.rosenoire.regy.pipeline.datagen.DataGenObject;
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
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

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
        LogEntry.of(this)
                .info("|> INITIALIZING CLIENT EVENTS")
                .send();

        registerAllBlockEntryRenderModes();
    }

    private void registerAllBlockEntryRenderModes() {
        var log = LogEntry.of(this);
        log.info("§white|§end--§cyan|>§end Registering all BlockEntry RenderModes...");

        var entries = new ArrayList<BlockEntry<?>>();

        for (Entry<?> entry : this.instance.entries()) {
            if (entry instanceof BlockEntry<?> blockEntry) {
                entries.add(blockEntry);
            }
        }

        log.info("§white|§end--§cyan|§end  Detected {} potential entries", entries.size());
        log.send();

        entries.forEach(this::registerBlockEntryRenderMode);
    }

    private void registerBlockEntryRenderMode(BlockEntry<?> blockEntry) {
        var identifier = blockEntry.identifier();
        var renderMode = blockEntry.renderMode;

        if (renderMode != BlockRenderMode.SOLID) {
            LogEntry.of(this)
                    .info("§white|§end--§cyan|§end  > §green\"{}\"§end §white{}§end", identifier, renderMode)
                    .send();

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

    protected final List<ClientEntryBuilderAdapter> entryBuilderAdapters = new ArrayList<>();

    public @Nullable NonNullSupplier<DataGenObject> getAdapterForEntry(@NonNull Entry<?> entry) {
        for (var adapter : this.entryBuilderAdapters) {
            var builder = adapter.getSupplier(this, entry);
            if (builder != null) return builder;
        }

        return null;
    }

    public SELF registerEntryBuilderAdapter(@NonNull ClientEntryBuilderAdapter adapter) {
        LogEntry.of(this)
                .info(
                        "|> §white{}:§end Registered §cyanClientEntryBuilderAdapter§end §blue{}§end.",
                        this.getClass().getSimpleName(),
                        adapter.getClass().getSimpleName()
                )
                .send();

        this.entryBuilderAdapters.add(adapter);
        return this.self();
    }

    // TODO: There's gotta be a better way of doing that but it's like 23:10 right now and i'm exhausted and I really
    //  couldn't be bothered to make it better LOL (this is so bad :sad:).
    protected <B extends AbstractClientEntryBuilder<E, V>, E extends Entry<V>, V> B genericEntryBuilder(
            E entry,
            BiFunction<AbstractClientRegy<R, SELF>, E, B> func
    ) {
        NonNullSupplier<DataGenObject> supplier = () -> func.apply(this, entry);
        var dataObject = this.dataGeneration().getOrCreateData(entry.regyIdentifier().hashCode(), supplier);
        //noinspection unchecked
        return (B) dataObject;
    }

    public <I extends Item> ClientItemEntryBuilder<I> item(ItemEntry<I> itemEntry) {
        return this.genericEntryBuilder(itemEntry, ClientItemEntryBuilder::new);
    }

    public <B extends Block> ClientBlockEntryBuilder<B> block(BlockEntry<B> blockEntry) {
        return this.genericEntryBuilder(blockEntry, ClientBlockEntryBuilder::new);
    }

    public ClientPotionEntryBuilder potion(PotionEntry potionEntry) {
        return this.genericEntryBuilder(potionEntry, ClientPotionEntryBuilder::new);
    }

    public ClientSoundEntryBuilder sound(SoundEntry soundEntry) {
        return this.genericEntryBuilder(soundEntry, ClientSoundEntryBuilder::new);
    }

    public ConditionalItemModelPropertyBuilder conditionalItemModelProperty(String identifier) {
        return new ConditionalItemModelPropertyBuilder(this, identifier);
    }

    public SelectItemModelPropertyBuilder selectItemModelProperty(String identifier) {
        return new SelectItemModelPropertyBuilder(this, identifier);
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
            LogEntry.of(this)
                    .error("Cannot run datagen because it was already run!")
                    .send();

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
