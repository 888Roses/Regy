package dev.rosenoire.regy.pipeline;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface RegyInternal {
    List<AbstractRegy<?>> REGIES = new ArrayList<>();

    static Optional<AbstractRegy<?>> regyFromNamespace(@NonNull String namespace) {
        return REGIES
                .stream()
                .filter(instance -> instance.modNamespace().equals(namespace))
                .findFirst();
    }

    static Optional<AbstractRegy<?>> regyFromIdentifier(@NonNull Identifier identifier) {
        return regyFromNamespace(identifier.getNamespace());
    }

    static Optional<AbstractRegy<?>> regyFromKey(@NonNull ResourceKey<?> key) {
        return regyFromIdentifier(key.identifier());
    }

    static Optional<AbstractRegy<?>> regyFromReference(Holder.@NonNull Reference<?> reference) {
        return regyFromKey(reference.key());
    }
}
