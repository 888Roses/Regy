package dev.rosenoire.regy.pipeline;

public abstract class RegyInstance<R extends RegyInstance<R>> {
    /// Namespace of the mod this Regy getOwner represents.
    protected final String modNamespace;

    protected RegyInstance(String modNamespace) {
        this.modNamespace = modNamespace;
    }

    /// Returns this instance of [RegyInstance] cast as [R].
    public R self() {
        //noinspection unchecked
        return (R) this;
    }
}
