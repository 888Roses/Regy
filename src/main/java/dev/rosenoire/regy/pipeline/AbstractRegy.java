package dev.rosenoire.regy.pipeline;

import net.minecraft.resources.Identifier;

public abstract class AbstractRegy {
    /// Namespace of the mod this Regy instance represents.
    protected final String modNamespace;

    protected AbstractRegy(String modNamespace) {
        this.modNamespace = modNamespace;
    }

    /// Represents the namespace of the mod this Regy instance represents.
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
     *      - HudElementEntryBuilder<H> <H extends HudElement> hudElement(String path, H instance)
     *      - ParticleEntryBuilder<O> <O extends ParticleOptions> particle(String name, O options)
     *      - AttributeEntryBuilder attribute(String name)
     *
     *  **Client Side:**
     *      - <O extends ParticleOptions> ClientParticleEntryBuilder<O> particle(ParticleEntry<O> entry)
     */
}
