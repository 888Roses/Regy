package dev.rosenoire.regy.pipeline;

import dev.rosenoire.regy.api.event.WrappingValueEvent;
import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.factory.AxeItemFactory;
import dev.rosenoire.regy.pipeline.factory.BlockFactory;
import dev.rosenoire.regy.pipeline.factory.ItemFactory;
import dev.rosenoire.regy.pipeline.factory.SimpleItemFactory;
import dev.rosenoire.regy.pipeline.registration.Entry;
import dev.rosenoire.regy.pipeline.registration.block.BlockEntry;
import dev.rosenoire.regy.pipeline.registration.block.BlockEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.component.DataComponentEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.group.CreativeTabEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.group.CreativeTabMapper;
import dev.rosenoire.regy.pipeline.registration.item.material.ToolMaterialEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.potion.PotionEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.sound.SoundEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.tag.item.ItemTagEntryBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static dev.rosenoire.regy.pipeline.content.BlockTransformers.nonFullBlock;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public abstract class AbstractRegy<R extends AbstractRegy<R>> extends RegyInstance<R> {
    // region internal
    private final HashMap<Identifier, Entry<?>> entries = new HashMap<>();
    public final CreativeTabMapper creativeTabMapper = new CreativeTabMapper(this);

    protected final WrappingValueEvent<R> onSetupDatagen = WrappingValueEvent.create();
    protected final WrappingValueEvent<Entry<?>> onEntryAdded = WrappingValueEvent.create();

    protected AbstractRegy(String modNamespace) {
        super(modNamespace);
    }

    public <A, E extends Entry<A>> E entry(@NonNull E entry) {
        this.onEntryAdded.before().accept(entry);
        this.entries.put(entry.regyIdentifier(), entry);
        this.onEntryAdded.after().accept(entry);
        return entry;
    }

    // endregion

    // region modifiers

    // TODO: Documentation!
    public R onBeforeSetupDatagen(@NonNull Consumer<@NonNull R> subscriber) {
        this.onSetupDatagen.before().subscribe(subscriber);
        return self();
    }

    // TODO: Documentation!
    public R onAfterSetupDatagen(@NonNull Consumer<@NonNull R> subscriber) {
        this.onSetupDatagen.after().subscribe(subscriber);
        return self();
    }

    // TODO: Documentation!
    public R onBeforeEntryAdded(@NonNull Consumer<Entry<?>> subscriber) {
        this.onEntryAdded.before().subscribe(subscriber);
        return self();
    }

    // TODO: Documentation!
    public R onAfterEntryAdded(@NonNull Consumer<Entry<?>> subscriber) {
        this.onEntryAdded.after().subscribe(subscriber);
        return self();
    }

    // TODO: Documentation!
    public <B extends RegyOwnable> B map(Function<R, B> mapper) {
        return mapper.apply(self());
    }

    // endregion

    // region setup

    public void initializeEvents() {
        this.creativeTabMapper.initializeEvents();
    }

    // endregion

    // region access

    /// Represents the namespace of the mod this Regy getOwner represents.
    public String modNamespace() {
        return this.modNamespace;
    }

    /// Creates an [Identifier] using the [#modNamespace()] and the given `path`.
    public Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(modNamespace(), path);
    }

    public Collection<Entry<?>> entries() {
        return this.entries.values();
    }

    public Optional<Entry<?>> entryByIdentifier(Identifier identifier) {
        return Optional.ofNullable(this.entries.getOrDefault(identifier, null));
    }

    // endregion

    // region registry

    public CreativeTabEntryBuilder<R> tab(String identifier) {
        return new CreativeTabEntryBuilder<>(self(), self(), identifier);
    }

    public <I extends Item, P> ItemEntryBuilder<I, P> item(String identifier, P parent, ItemFactory<I> factory) {
        return new ItemEntryBuilder<>(self(), parent, identifier, factory);
    }

    public <I extends Item> ItemEntryBuilder<I, R> item(String identifier, ItemFactory<I> factory) {
        return item(identifier, self(), factory);
    }

    public <I extends Item> ItemEntryBuilder<I, R> item(String identifier, SimpleItemFactory<I> factory) {
        return item(identifier, (ItemFactory<I>) factory);
    }

    public ItemEntryBuilder<Item, R> item(String identifier) {
        return item(identifier, Item::new);
    }

    public <I extends AxeItem> ItemEntryBuilder<I, R> axe(String identifier, AxeItemFactory<I> factory) {
        return item(identifier, factory);
    }

    public ToolMaterialEntryBuilder<R> material(String identifier) {
        return new ToolMaterialEntryBuilder<>(self(), self(), identifier);
    }

    public SoundEntryBuilder<R> sound(String identifier) {
        return new SoundEntryBuilder<>(self(), self(), identifier);
    }

    public PotionEntryBuilder<R> potion(String identifier, String name) {
        return new PotionEntryBuilder<>(self(), self(), identifier, name);
    }

    public ItemTagEntryBuilder<R> itemTag(String identifier) {
        return new ItemTagEntryBuilder<>(self(), self(), identifier);
    }

    public <V> DataComponentEntryBuilder<V, R> component(String identifier) {
        return new DataComponentEntryBuilder<>(self(), self(), identifier);
    }

    public <B extends Block> BlockEntryBuilder<B, R> block(String identifier, BlockFactory<B> factory) {
        return new BlockEntryBuilder<>(self(), self(), identifier, factory);
    }

    public BlockEntryBuilder<Block, R> block(String identifier) {
        return block(identifier, Block::new);
    }

    public BlockEntryBuilder<StairBlock, R> stairs(String identifier, BlockState source) {
        return block(identifier, properties -> new StairBlock(source, properties)).tag(BlockTags.STAIRS);
    }

    public BlockEntryBuilder<StairBlock, R> stairs(String identifier, Block source) {
        return stairs(identifier, source.defaultBlockState());
    }

    public BlockEntryBuilder<StairBlock, R> stairs(String identifier, BlockEntry<?> source) {
        return stairs(identifier, source.get());
    }

    public BlockEntryBuilder<TrapDoorBlock, R> trapdoor(String identifier, BlockSetType blockSetType) {
        return block(identifier, properties -> new TrapDoorBlock(blockSetType, properties))
                .transform(nonFullBlock())
                .tag(BlockTags.TRAPDOORS)
                .trapdoorModel()
                .cutout();
    }

    public BlockEntryBuilder<DoorBlock, R> door(String identifier, BlockSetType blockSetType) {
        return block(identifier, properties -> new DoorBlock(blockSetType, properties))
                .transform(nonFullBlock())
                .tag(BlockTags.DOORS)
                .doorModel()
                .cutout();
    }

    // endregion

    // region datagen

    protected final DataGeneration dataGeneration = new DataGeneration(this);
    protected FabricDataGenerator.Pack datagenPack;

    /// Represents the [FabricDataGenerator.Pack] data-gen pack targeted by this
    /// [AbstractRegy] instance after
    /// [#setupDatagen(FabricDataGenerator.Pack)] was called.
    public FabricDataGenerator.Pack pack() {
        return datagenPack;
    }

    /// Represents the manager for all data generation for this [AbstractRegy] instance.
    public DataGeneration dataGeneration() {
        return this.dataGeneration;
    }

    public void setupDatagen(FabricDataGenerator.Pack datagenPack) {
        this.onSetupDatagen.before().accept(self());
        this.datagenPack = datagenPack;
        this.onSetupDatagen.after().accept(self());
    }

    // endregion
}