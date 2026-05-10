package dev.rosenoire.regy.pipeline.registration;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.RegyOwnable;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

/// Represents an abstract builder of an [AbstractRegistryEntry] representing a value registered in a Minecraft registry.
/// @param <T> Type of the value represented by this entry builder.
/// @param <P> Represents the type of the parent of this [AbstractEntryBuilder] if it has one. Usually also an [AbstractEntryBuilder] but doesn't have to be.
public abstract class AbstractEntryBuilder<T extends Entry<?>, P> implements RegyOwnable {
    private final @NonNull AbstractRegy<?> regy;
    private final P parent;
    private final String identifier;

    public AbstractEntryBuilder(@NonNull AbstractRegy<?> regy, P parent, String identifier) {
        this.regy = regy;
        this.parent = parent;
        this.identifier = identifier;
    }

    public final @NonNull AbstractRegy<?> getRegy() {
        return this.regy;
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
        return getRegy().id(path());
    }

    /// Registers this builder in the Minecraft registries and returns an [AbstractRegistryEntry]
    /// representing the registered value.
    public abstract @NonNull T register();

    protected abstract Identifier regyIdentifier();

    protected Identifier getRegyIdentifierFromRegistry(ResourceKey<?> resourceKey) {
        return this.identifier().withPrefix(resourceKey.registry().getPath() + "/");
    }

    /// Cannot be called before registration!
    public Optional<T> entry() {
        return getRegy().entryByIdentifier(this.regyIdentifier()).map(entry -> {
            try {
                //noinspection unchecked
                return (T) entry;
            }
            catch (Exception ignoredException) {
                return null;
            }
        });
    }

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