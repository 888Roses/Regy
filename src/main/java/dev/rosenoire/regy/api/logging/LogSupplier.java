package dev.rosenoire.regy.api.logging;

import dev.rosenoire.regy.api.data.NonNullSupplier;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

public interface LogSupplier extends NonNullSupplier<Logger> {
    @NonNull Logger log();

    @Override
    default @NonNull Logger get() {
        return this.log();
    }
}
