package dev.rosenoire.regy.pipeline.registration.tag;

import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface TagDefinition<E, T> {
    TagDefinition<E, T> add(E object);

    default TagDefinition<E, T> add(E... objects) {
        return this.addAll(Arrays.stream(objects));
    }

    default TagDefinition<E, T> addAll(Collection<E> collection) {
        collection.forEach(this::add);
        return this;
    }

    default TagDefinition<E, T> addAll(Stream<E> stream) {
        stream.forEach(this::add);
        return this;
    }

    TagDefinition<E, T> addOptional(E object);

    TagDefinition<E, T> addTag(TagKey<T> tagKey);

    TagDefinition<E, T> addOptionalTag(TagKey<T> tagKey);

    // TODO: Make
    default TagDefinition<E, T> setReplace(boolean replace) {
        return this;
    }

    // TODO: Make
    default TagDefinition<E, T> forceAddTag(TagKey<T> tag) {
        return this;
    }

    static <T> TagDefinition<ResourceKey<T>, T> forBuilder(TagBuilder tagBuilder) {
        return new TagDefinition<>() {
            public TagDefinition<ResourceKey<T>, T> add(ResourceKey<T> resourceKey) {
                tagBuilder.addElement(resourceKey.identifier());
                return this;
            }

            public TagDefinition<ResourceKey<T>, T> addOptional(ResourceKey<T> resourceKey) {
                tagBuilder.addOptionalElement(resourceKey.identifier());
                return this;
            }

            @Override
            public TagDefinition<ResourceKey<T>, T> addTag(TagKey<T> tagKey) {
                tagBuilder.addTag(tagKey.location());
                return this;
            }

            @Override
            public TagDefinition<ResourceKey<T>, T> addOptionalTag(TagKey<T> tagKey) {
                tagBuilder.addOptionalTag(tagKey.location());
                return this;
            }
        };
    }

    default <U> TagDefinition<U, T> map(Function<U, E> function) {
        final TagDefinition<E, T> TagDefinition = this;
        return new TagDefinition<>() {
            @Override
            public TagDefinition<U, T> add(U object) {
                TagDefinition.add(function.apply(object));
                return this;
            }

            @Override
            public TagDefinition<U, T> addOptional(U object) {
                TagDefinition.add(function.apply(object));
                return this;
            }

            @Override
            public TagDefinition<U, T> addTag(TagKey<T> tagKey) {
                TagDefinition.addTag(tagKey);
                return this;
            }

            @Override
            public TagDefinition<U, T> addOptionalTag(TagKey<T> tagKey) {
                TagDefinition.addOptionalTag(tagKey);
                return this;
            }
        };
    }
}
