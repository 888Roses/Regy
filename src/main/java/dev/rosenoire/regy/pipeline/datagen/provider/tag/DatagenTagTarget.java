package dev.rosenoire.regy.pipeline.datagen.provider.tag;

import net.minecraft.tags.TagKey;

import java.util.List;

public interface DatagenTagTarget {
    List<TagKey<?>> getAllTags();
}
