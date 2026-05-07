package dev.rosenoire.regy.pipeline.datagen.provider.tag;

import dev.rosenoire.regy.pipeline.datagen.DatagenTarget;
import net.minecraft.tags.TagKey;

import java.util.List;

public interface DatagenTagTarget extends DatagenTarget {
    List<TagKey<?>> getAllTags();
}
