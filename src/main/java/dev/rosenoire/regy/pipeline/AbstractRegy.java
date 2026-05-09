package dev.rosenoire.regy.pipeline;

import dev.rosenoire.regy.api.event.WrappingValueEvent;
import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.factory.AxeItemFactory;
import dev.rosenoire.regy.pipeline.factory.ItemFactory;
import dev.rosenoire.regy.pipeline.factory.SimpleItemFactory;
import dev.rosenoire.regy.pipeline.registration.Entry;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.group.CreativeTabEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.group.CreativeTabMapper;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemMaps;
import dev.rosenoire.regy.pipeline.registration.item.material.ToolMaterialEntryBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractRegy<R extends AbstractRegy<R>> {
    // region internal

    /// Namespace of the mod this Regy getOwner represents.
    protected final String modNamespace;

    private final List<Entry<?>> entries = new ArrayList<>();
    public final CreativeTabMapper creativeTabMapper = new CreativeTabMapper(this);

    protected final WrappingValueEvent<R> onSetupDatagen = WrappingValueEvent.create();
    protected final WrappingValueEvent<Entry<?>> onEntryAdded = WrappingValueEvent.create();

    protected AbstractRegy(String modNamespace) {
        this.modNamespace = modNamespace;
    }

    public <A, E extends Entry<A>> E entry(@NonNull E entry) {
        this.onEntryAdded.before().accept(entry);
        this.entries.add(entry);
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
    public <B extends RegyOwnable> B map(Function<AbstractRegy<R>, B> mapper) {
        return mapper.apply(this);
    }

    // endregion

    // region setup

    public void initializeEvents() {
        this.creativeTabMapper.initializeEvents();
    }

    // endregion

    // region access

    @SuppressWarnings("unchecked")
    public R self() {
        return (R) this;
    }

    /// Represents the namespace of the mod this Regy getOwner represents.
    public String modNamespace() {
        return this.modNamespace;
    }

    /// Creates an [Identifier] using the [#modNamespace()] and the given `path`.
    public Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(modNamespace(), path);
    }

    public List<Entry<?>> entries() {
        return this.entries;
    }

    // endregion

    // region registry

    /*
     *  TODO: Expected Features:
     *  **Common Side:**
     *      [~] - <I extends Item> ItemEntryBuilder<I> item(String name, ItemFactory<I> factory)
     *          [X] - <E, T extends ComponentType<E>> ItemEntryBuilder<I> component(E value)
     *          [X] - ItemEntryBuilder<I> attackDamage(float attackDamage)
     *          [X] - ItemEntryBuilder<I> attackSpeed(float attackSpeed)
     *      - <B extends Block> BlockEntryBuilder<B> block(String name, BlockFactory<B> factory)
     *      - <F extends Fluid> FluidEntryBuilder<F> fluid(String name, FluidFactory<F> factory)
     *      - <E extends Entity> EntityEntryBuilder<E> entity(String name, EntityFactory<E> factory)
     *      - SoundEntryBuilder sound(String name)
     *      - CommandEntryBuilder command(String name, String root)
     *      - HudElementEntryBuilder<H> <H extends HudElement> hudElement(String path, H getOwner)
     *      - ParticleEntryBuilder<O> <O extends ParticleOptions> particle(String name, O options)
     *      - AttributeEntryBuilder attribute(String name)
     *      [X] - CreativeTabEntryBuilder creativeTab(String name)
     *      - ToolMaterialEntryBuilder material(String name)
     *      - DamageType
     *      - Packets?
     *      - Trims
     * 
     *
     *  **Client Side:**
     *      - <O extends ParticleOptions> ClientParticleEntryBuilder<O> particle(ParticleEntry<O> entry)
     */

    public CreativeTabEntryBuilder<AbstractRegy<R>> tab(String identifier) {
        return new CreativeTabEntryBuilder<>(this, this, identifier);
    }

    public <I extends Item> ItemEntryBuilder<I, AbstractRegy<R>> item(String identifier, ItemFactory<I> factory) {
        return new ItemEntryBuilder<>(this, this, identifier, factory);
    }

    public <I extends Item> ItemEntryBuilder<I, AbstractRegy<R>> item(String identifier, SimpleItemFactory<I> factory) {
        return item(identifier, (ItemFactory<I>) factory);
    }

    public ItemEntryBuilder<Item, AbstractRegy<R>> item(String identifier) {
        return item(identifier, Item::new);
    }

    public <I extends AxeItem> ItemEntryBuilder<I, AbstractRegy<R>> axe(String identifier, AxeItemFactory<I> factory) {
        return item(identifier, factory);
    }

    public ToolMaterialEntryBuilder<AbstractRegy<R>> material(String identifier) {
        return new ToolMaterialEntryBuilder<>(this, this, identifier);
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
