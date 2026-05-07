package dev.rosenoire.regy.pipeline;

import dev.rosenoire.regy.pipeline.datagen.v1.ConflictMode;
import dev.rosenoire.regy.pipeline.datagen.v1.DatagenTarget;
import dev.rosenoire.regy.pipeline.datagen.v1.ProviderContext;
import dev.rosenoire.regy.pipeline.datagen.v1.ProviderType;
import dev.rosenoire.regy.pipeline.datagen.v1.provider.RegyDatagenProvider;
import dev.rosenoire.regy.pipeline.factory.ItemFactory;
import dev.rosenoire.regy.pipeline.registration.AbstractRegistryEntry;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.group.CreativeTabEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.item.group.CreativeTabMapper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRegy<R extends AbstractRegy<R>> {
    /// Namespace of the mod this Regy getOwner represents.
    protected final String modNamespace;
    protected FabricDataGenerator.Pack datagenPack;

    public final Map<DatagenTarget, Object> datagenTargets = new HashMap<>();
    protected final List<RegyDatagenProvider> datagenProviders = new ArrayList<>();
    public final List<AbstractRegistryEntry<?, ?>> entries = new ArrayList<>();
    public final CreativeTabMapper creativeTabMapper = new CreativeTabMapper(this);

    protected ConflictMode conflictMode = ConflictMode.THROW; // TODO

    protected AbstractRegy(String modNamespace) {
        this.modNamespace = modNamespace;
    }

    public void initializeEvents() {
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register(this.creativeTabMapper::modifyEntriesAll);
    }

    /// Represents the namespace of the mod this Regy getOwner represents.
    public String modNamespace() {
        return this.modNamespace;
    }

    /// Creates an [Identifier] using the [#modNamespace()] and the given `path`.
    public Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(modNamespace(), path);
    }

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
     *
     *  **Client Side:**
     *      - <O extends ParticleOptions> ClientParticleEntryBuilder<O> particle(ParticleEntry<O> entry)
     */

    public <A, B, E extends AbstractRegistryEntry<A, B>> E process(AbstractEntryBuilder<?, ?> builder, E entry) {
        this.datagenTargets.put(builder, entry);
        this.entries.add(entry);

        // TODO: Implement postprocessors
        return entry;
    }

    public CreativeTabEntryBuilder<AbstractRegy<R>> tab(String identifier) {
        return new CreativeTabEntryBuilder<>(this, this, identifier);
    }

    public <I extends Item> ItemEntryBuilder<I, AbstractRegy<R>> item(String identifier, ItemFactory<I> factory) {
        return new ItemEntryBuilder<>(this, this, identifier, factory);
    }

    public ItemEntryBuilder<Item, AbstractRegy<R>> item(String identifier) {
        return item(identifier, Item::new);
    }

    // region datagen

    public void setupDatagen(FabricDataGenerator.Pack datagenPack, ConflictMode conflictMode) {
        this.datagenPack = datagenPack;

        for (var value : ProviderType.values()) {
            datagenPack.addProvider((output, registriesFuture) -> {
                var ctx = new ProviderContext(this, output, registriesFuture);
                return value.getProviderFactory().bake(ctx);
            });
        }

        this.setConflictMode(conflictMode);
    }

    public void setupDatagen(FabricDataGenerator.Pack datagenPack) {
        this.setupDatagen(datagenPack, ConflictMode.THROW);
    }

    public void setConflictMode(ConflictMode conflictMode) {
        this.conflictMode = conflictMode;
    }

    public ConflictMode getConflictMode() {
        return conflictMode;
    }

    // endregion
}
