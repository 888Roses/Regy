package dev.rosenoire.regy.pipeline.datagen;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.RegyOwnable;
import org.jspecify.annotations.NonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class PostProcessTargetStorage implements RegyOwnable {
    private final @NonNull AbstractRegy<?> instance;
    private final HashSet<Object> storage = new HashSet<>();

    public PostProcessTargetStorage(@NonNull AbstractRegy<?> instance) {
        this.instance = instance;
    }

    public int size() {
        return this.storage.size();
    }

    public void addPostProcessTarget(Object target) {
        if (target == null) {
            this.log().warn("Attempted to add a null post process target. This is not allowed!");
            return;
        }

        this.storage.add(target);

        this.log().info(
                "Successfully added post process target ({}).",
                target.getClass().getSimpleName()
        );
    }

    public Stream<Object> getPostProcessTargets() {
        return this.storage.stream();
    }

    public <T> List<T> getPostProcessTargetsOfClass(Class<T> clazz) {
        //noinspection unchecked
        return this.getPostProcessTargets()
                .map(target -> target.getClass().isAssignableFrom(clazz) ? (T) target : null)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public @NonNull AbstractRegy<?> regy() {
        return this.instance;
    }
}
