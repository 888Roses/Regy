package dev.rosenoire.regy.pipeline.registration;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.datagen.DatagenTarget;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

/// Represents an abstract builder of an [AbstractRegistryEntry] representing a value registered in a Minecraft registry.
/// @param <T> Type of the value represented by this entry builder.
/// @param <P> Represents the type of the parent of this [AbstractEntryBuilder] if it has one. Usually also an [AbstractEntryBuilder] but doesn't have to be.
public abstract class AbstractEntryBuilder<T extends Entry<?>, P> implements DatagenTarget {
    private final @NonNull AbstractRegy<?> owner;
    private final P parent;
    private final String identifier;

    public AbstractEntryBuilder(@NonNull AbstractRegy<?> owner, P parent, String identifier) {
        this.owner = owner;
        this.parent = parent;
        this.identifier = identifier;
    }

    /// [AbstractRegy] getOwner that created this entry builder.
    protected final @NonNull AbstractRegy<?> getOwner() {
        return this.owner;
    }

    /// Returns the instanceof [P] that created this [AbstractEntryBuilder].
    public final P getParent() {
        return this.parent;
    }

    /// Returns the `String` identifier of this [AbstractEntryBuilder].
    public final String path() {
        return identifier;
    }

    /// Returns an [Identifier] representing this [AbstractEntryBuilder] using its getOwner
    /// [AbstractRegy]'s [AbstractRegy#id(String)] and the [#path()].
    public final Identifier identifier() {
        return getOwner().id(path());
    }

    /// Registers this builder in the Minecraft registries and returns an [AbstractRegistryEntry]
    /// representing the registered value.
    public abstract @NonNull T register();

    /// Registers this builder in the Minecraft registries and returns the parent of this
    /// builder.
    public P build() {
        register();
        return getParent();
    }

    protected final IllegalStateException throwRegisterNullEntryException() {
        return new IllegalStateException("Failed to process item entry for \"{" + this.identifier() + "}\"! Processed value was null!");
    }
}