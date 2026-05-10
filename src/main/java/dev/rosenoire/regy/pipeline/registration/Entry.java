package dev.rosenoire.regy.pipeline.registration;

import dev.rosenoire.regy.api.data.NonNullSupplier;
import net.minecraft.resources.Identifier;

public interface Entry<T> extends NonNullSupplier<T> {
    Identifier regyIdentifier();
}
