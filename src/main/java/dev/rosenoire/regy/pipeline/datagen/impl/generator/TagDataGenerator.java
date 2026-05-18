package dev.rosenoire.regy.pipeline.datagen.impl.generator;

import dev.rosenoire.regy.pipeline.datagen.DataGenerator;
import dev.rosenoire.regy.pipeline.registration.tag.TagDefinition;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;

import java.util.function.UnaryOperator;

public interface TagDataGenerator<T, G extends TagDataGenerator<T, G>> extends DataGenerator {
    default G self() {
        //noinspection unchecked
        return (G) this;
    }

    G tag(TagKey<T> tag, UnaryOperator<TagDefinition<T, T>> func);

    default <A, B> TagDefinition<A, B> transform(TagAppender<A, B> appender) {
        return new TagDefinition<>() {
            @Override
            public TagDefinition<A, B> add(A object) {
                appender.add(object);
                return this;
            }

            @Override
            public TagDefinition<A, B> addOptional(A object) {
                appender.addOptional(object);
                return this;
            }

            @Override
            public TagDefinition<A, B> addTag(TagKey<B> tagKey) {
                appender.addTag(tagKey);
                return this;
            }

            @Override
            public TagDefinition<A, B> addOptionalTag(TagKey<B> tagKey) {
                appender.addOptionalTag(tagKey);
                return this;
            }
        };
    }
}
