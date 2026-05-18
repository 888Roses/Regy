package dev.rosenoire.regy.pipeline.registration.tag;

import dev.rosenoire.regy.api.data.NonNullConsumer;
import net.minecraft.tags.TagKey;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record GenericTagPostProcessTarget(
        @NonNull String generatorName,
        TagKey key,
        List<@NonNull NonNullConsumer<TagDefinition>> instructionStorage
) {
}