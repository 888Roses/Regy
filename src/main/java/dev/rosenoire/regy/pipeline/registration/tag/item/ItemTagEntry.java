package dev.rosenoire.regy.pipeline.registration.tag.item;

import dev.rosenoire.regy.pipeline.registration.tag.AbstractTagEntry;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

public class ItemTagEntry extends AbstractTagEntry<Item> {
    public ItemTagEntry(@NonNull TagKey<Item> value, Identifier regyIdentifier) {
        super(value, regyIdentifier);
    }
}
