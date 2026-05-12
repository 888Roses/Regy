package dev.rosenoire.regy.pipeline.datagen.impl.generator;

import dev.rosenoire.regy.pipeline.datagen.DataGenerator;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;

import java.util.function.UnaryOperator;

public interface TagDataGenerator<T, G extends TagDataGenerator<T, G>> extends DataGenerator {
    default G self() {
        //noinspection unchecked
        return (G) this;
    }

    G tag(TagKey<T> tag, UnaryOperator<TagAppender<T, T>> func);
}
