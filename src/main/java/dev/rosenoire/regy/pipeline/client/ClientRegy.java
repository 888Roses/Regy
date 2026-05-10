package dev.rosenoire.regy.pipeline.client;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import org.jspecify.annotations.NonNull;

public class ClientRegy<R extends AbstractRegy<R>> extends AbstractClientRegy<R> {
    protected ClientRegy(@NonNull R instance) {
        super(instance);
    }

    public static <R extends AbstractRegy<R>> ClientRegy<R> create(@NonNull R instance) {
        return new ClientRegy<>(instance);
    }
}
