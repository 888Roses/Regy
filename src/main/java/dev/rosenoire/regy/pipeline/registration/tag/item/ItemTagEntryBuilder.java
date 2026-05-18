package dev.rosenoire.regy.pipeline.registration.tag.item;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.DataGenerators;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.ItemTagDataGenerator;
import dev.rosenoire.regy.pipeline.registration.tag.AbstractTagEntryBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

public class ItemTagEntryBuilder<P> extends AbstractTagEntryBuilder<P, Item, ItemTagEntry, ItemTagDataGenerator, ItemTagEntryBuilder<P>> {
    public ItemTagEntryBuilder(@NonNull AbstractRegy<?> regy, P parent, String identifier) {
        super(regy, parent, identifier, Registries.ITEM, ItemTagEntry::new, DataGenerators.ITEM_TAGS);
    }

    public ItemTagEntryBuilder<P> item(Item... items) {
        //noinspection unchecked
        return instruction(builder -> builder.add(items));
    }

    public ItemTagEntryBuilder<P> tag(TagKey<Item> itemTag) {
        //noinspection unchecked
        return instruction(builder -> builder.addTag(itemTag));
    }

    public ItemTagEntryBuilder<P> tag(ItemTagEntry entry) {
        return tag(entry.get());
    }

    public ItemTagEntryBuilder<P> optional(Item item) {
        //noinspection unchecked
        return instruction(builder -> builder.addOptional(item));
    }

    public ItemTagEntryBuilder<P> optionalTag(TagKey<Item> itemTag) {
        //noinspection unchecked
        return instruction(builder -> builder.addOptionalTag(itemTag));
    }

    public ItemTagEntryBuilder<P> optionalTag(ItemTagEntry entry) {
        return optionalTag(entry.get());
    }
}
