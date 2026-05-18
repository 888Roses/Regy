package dev.rosenoire.regy.pipeline.registration.tag;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

public @FunctionalInterface interface GenericTagFactory<TYPE, ENTRY extends AbstractTagEntry<TYPE>> {
    ENTRY bake(TagKey<TYPE> key, Identifier regyIdentifier, Identifier identifier);
}
