package dev.rosenoire.regy.pipeline.registration.tag;

import dev.rosenoire.regy.pipeline.registration.AbstractSimpleEntry;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import org.jspecify.annotations.NonNull;

public abstract class AbstractTagEntry<T> extends AbstractSimpleEntry<TagKey<T>> {
    public AbstractTagEntry(@NonNull TagKey<T> value, @NonNull Identifier regyIdentifier, @NonNull Identifier identifier) {
        super(value, regyIdentifier, identifier);
    }
}