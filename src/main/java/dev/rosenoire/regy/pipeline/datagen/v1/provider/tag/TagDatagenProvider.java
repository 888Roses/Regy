package dev.rosenoire.regy.pipeline.datagen.v1.provider.tag;

import dev.rosenoire.regy.pipeline.datagen.v1.ProviderContext;
import dev.rosenoire.regy.pipeline.datagen.v1.ProviderType;
import dev.rosenoire.regy.pipeline.datagen.v1.provider.DefaultDatagenProvider;
import dev.rosenoire.regy.pipeline.registration.Entry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Optional;

public interface TagDatagenProvider<T, E extends Entry<? extends T>> extends DefaultDatagenProvider {
    TagAppender<T, T> valueLookupBuilder(TagKey<T> key);

    @ApiStatus.NonExtendable
    default void runTagPostProcessors(HolderLookup.@NonNull Provider wrapperLookup, DatagenTagTarget tagTarget) {
        var map = new HashMap<ProviderType, DatagenTagTarget.TagPostProcessor>();
        tagTarget.registerTagPostProcessors(map::put);

        Optional
                .ofNullable(map.getOrDefault(getType(), null))
                .ifPresent(postProcessor -> postProcessor.modifyTags(wrapperLookup, this::valueLookupBuilder));
    }

    ResourceKey<? extends Registry<T>> getRegistry();

    @Nullable E mapDatagenEntry(@NonNull Object object);

    default void generateTagsForEntry(ProviderContext context, HolderLookup.@NonNull Provider wrapperLookup) {
        for (var datagenEntry : context.getOwner().datagenTargets.entrySet()) {
            //  entry.getValue() instanceof ItemEntry<? extends Item> itemEntry
            if (datagenEntry.getKey() instanceof DatagenTagTarget target) {
                Optional.ofNullable(mapDatagenEntry(datagenEntry.getValue()))
                        .ifPresent(entry -> {
                            target.getAllTags()
                                    .stream()
                                    .map(tagKey -> tagKey.cast(getRegistry()))
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .forEach(tagKey ->
                                            this.valueLookupBuilder(tagKey)
                                                    .add(entry.get())
                                                    .setReplace(false)
                                    );
                        });

                runTagPostProcessors(wrapperLookup, target);
            }
        }
    }
}
