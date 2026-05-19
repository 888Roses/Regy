package dev.rosenoire.regy.api.logging;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

public interface LogSupplier {
    @NonNull Logger log();
}
