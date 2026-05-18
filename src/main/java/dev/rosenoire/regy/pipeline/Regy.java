package dev.rosenoire.regy.pipeline;

/// Represents a default implementation of the [AbstractRegy] class.
/// This class is `final`; if you wish to extend the behaviour of a Regy registry,
/// extend [AbstractRegy] instead.
public final class Regy extends AbstractRegy<Regy> {
    private Regy(String modNamespace) {
        super(modNamespace);
    }

    /// Creates a new getOwner of the default Regy class using the given `namespace`
    /// as the mod namespace for registration, etc.
    public static Regy create(String modNamespace) {
        return new Regy(modNamespace);
    }
}
