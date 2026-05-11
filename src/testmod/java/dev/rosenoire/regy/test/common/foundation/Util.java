package dev.rosenoire.regy.test.common.foundation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

@ApiStatus.NonExtendable
public interface Util {
    static void forEachBlockPos(@NonNull AABB bounds, @NonNull Consumer<@NonNull BlockPos> consumer) {
        for (var x = bounds.minX; x < bounds.maxX; x++) {
            for (var y = bounds.minY; y < bounds.maxY; y++) {
                for (var z = bounds.minZ; z < bounds.maxZ; z++) {
                    consumer.accept(BlockPos.containing(x, y, z));
                }
            }
        }
    }
}
