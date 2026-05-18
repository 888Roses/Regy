package dev.rosenoire.regy.pipeline.client;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.RegyOwnable;
import org.jspecify.annotations.NonNull;

public interface ClientRegyOwnable extends RegyOwnable {
    @NonNull AbstractClientRegy<?, ?> client();

    @Override
    default @NonNull AbstractRegy<?> regy() {
        return this.client().regy();
    }
}
