package dev.rosenoire.regy.pipeline.registration.item.component;

import dev.rosenoire.regy.pipeline.registration.AbstractSimpleEntry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class DataComponentEntry<V> extends AbstractSimpleEntry<DataComponentType<V>> {
    public DataComponentEntry(@NonNull DataComponentType<V> value, Identifier regyIdentifier) {
        super(value, regyIdentifier);
    }
}
