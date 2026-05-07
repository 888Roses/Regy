package dev.rosenoire.regy.pipeline.datagen.provider.tag;

import dev.rosenoire.regy.pipeline.datagen.DatagenTarget;
import dev.rosenoire.regy.pipeline.datagen.ProviderType;
import dev.rosenoire.regy.pipeline.registration.Entry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface DatagenTagTarget extends DatagenTarget {
    List<TagKey<?>> getAllTags();

    default void registerTagPostProcessors(BiConsumer<ProviderType, TagPostProcessor> consumer) {
    }

    @FunctionalInterface
    interface TagPostProcessor {
        <T> void modifyTags(HolderLookup.Provider wrapperLookup, ValueLookupBuilder<T> valueLookupBuilder);
    }

    @FunctionalInterface
    interface ValueLookupBuilder<T> extends Function<TagKey<T>, TagAppender<T, T>> {
        TagAppender<T, T> valueLookupBuilder(TagKey<T> key);

        @Override
        default TagAppender<T, T> apply(TagKey<T> key) {
            return valueLookupBuilder(key);
        }
    }
}
