package dev.rosenoire.regy.pipeline;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

/**
 * Represents any object that can be owned by a {@link AbstractRegy}.
 */
public interface RegyOwnable {
    /**
     * {@link AbstractRegy} owner that created this instance.
     */
    @NonNull AbstractRegy<?> regy();

    default Logger log() {
        return this.regy().log;
    }
}
