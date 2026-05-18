package dev.rosenoire.regy.pipeline;

import dev.rosenoire.regy.api.logging.LogSupplier;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

/**
 * Represents any object that can be owned by a {@link AbstractRegy}.
 */
public interface RegyOwnable extends LogSupplier {
    /**
     * {@link AbstractRegy} owner that created this instance.
     */
    @NonNull AbstractRegy<?> regy();

    @Override
    default @NonNull Logger log() {
        return this.regy().log;
    }
}
