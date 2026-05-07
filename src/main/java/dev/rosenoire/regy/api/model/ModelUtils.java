package dev.rosenoire.regy.api.model;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public interface ModelUtils {
    static @NonNull Identifier getItemSubModelId(@NonNull Identifier identifier, @NonNull String suffix) {
        return identifier.withPath(path -> {
            String computedPath = "item/" + path;

            if (!suffix.isBlank()) {
                if (!suffix.strip().startsWith("_")) computedPath += "_";
                computedPath += suffix;
            }

            return computedPath;
        });
    }
}
