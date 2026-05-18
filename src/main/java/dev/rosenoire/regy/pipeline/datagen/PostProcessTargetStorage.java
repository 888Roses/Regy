package dev.rosenoire.regy.pipeline.datagen;

import dev.rosenoire.regy.api.logging.LogEntry;
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
            LogEntry.of(this)
                    .error("|> PostProcessTargetStorage: Attempted to add null Post process Target. This is not allowed!")
                    .send();

            return;
        }

        this.storage.add(target);

        LogEntry.of(this)
                .info("|> §whitePostProcessTargetStorage:§end Added Post Process Target §cyan{}§end.", target.getClass().getSimpleName())
                .send();
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
