package dev.rosenoire.regy.pipeline.registration;

import dev.rosenoire.regy.api.data.NonNullSupplier;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public interface Entry<T> extends NonNullSupplier<T> {
    @NonNull Identifier regyIdentifier();

    @NonNull Identifier identifier();
}
