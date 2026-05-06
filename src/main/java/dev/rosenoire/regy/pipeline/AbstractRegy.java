package dev.rosenoire.regy.pipeline;

import dev.rosenoire.regy.pipeline.datagen.filter.DatagenTarget;
import dev.rosenoire.regy.pipeline.datagen.ProviderContext;
import dev.rosenoire.regy.pipeline.datagen.ProviderType;
import dev.rosenoire.regy.pipeline.datagen.provider.AbstractDatagenProvider;
import dev.rosenoire.regy.pipeline.factory.ItemFactory;
import dev.rosenoire.regy.pipeline.registration.AbstractEntry;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import dev.rosenoire.regy.pipeline.registration.ItemEntryBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRegy<R extends AbstractRegy<R>> {
    /// Namespace of the mod this Regy owner represents.
    protected final String modNamespace;
    protected FabricDataGenerator.Pack datagenPack;

    public final List<DatagenTarget> datagenTargets = new ArrayList<>();
    protected final List<AbstractDatagenProvider> datagenProviders = new ArrayList<>();
    public final List<AbstractEntry<?, ?>> entries = new ArrayList<>();

    protected AbstractRegy(String modNamespace) {
        this.modNamespace = modNamespace;
    }

    /// Represents the namespace of the mod this Regy owner represents.
    public String modNamespace() {
        return this.modNamespace;
    }

    /// Creates an [Identifier] using the [#modNamespace()] and the given `path`.
    public Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(modNamespace(), path);
    }

    /*
     *  TODO: Expected Features:
     *  **Server Side:**
     *      - <I extends Item> ItemEntryBuilder<I> item(String name, ItemFactory<I> factory)
     *      - <B extends Block> BlockEntryBuilder<B> block(String name, BlockFactory<B> factory)
     *      - <F extends Fluid> FluidEntryBuilder<F> fluid(String name, FluidFactory<F> factory)
     *      - <E extends Entity> EntityEntryBuilder<E> entity(String name, EntityFactory<E> factory)
     *      - SoundEntryBuilder sound(String name)
     *      - CommandEntryBuilder command(String name, String root)
     *      - HudElementEntryBuilder<H> <H extends HudElement> hudElement(String path, H owner)
     *      - ParticleEntryBuilder<O> <O extends ParticleOptions> particle(String name, O options)
     *      - AttributeEntryBuilder attribute(String name)
     *
     *  **Client Side:**
     *      - <O extends ParticleOptions> ClientParticleEntryBuilder<O> particle(ParticleEntry<O> entry)
     */

    public <A, B, E extends AbstractEntry<A, B>> E process(AbstractEntryBuilder<?, ?> builder, E entry) {
        this.datagenTargets.add(builder);
        this.entries.add(entry);
        return entry;
    }

    public <I extends Item> ItemEntryBuilder<I, AbstractRegy<R>> item(String identifier, ItemFactory<I> factory) {
        return new ItemEntryBuilder<>(this, this, identifier, factory);
    }

    // region datagen

    public void setupDatagen(FabricDataGenerator.Pack datagenPack) {
        this.datagenPack = datagenPack;

        for (var value : ProviderType.values()) {
            datagenPack.addProvider((output, registriesFuture) -> {
                var ctx = new ProviderContext(this, output, registriesFuture);
                return value.getProviderFactory().bake(ctx);
            });
        }
    }

    // endregion
}
