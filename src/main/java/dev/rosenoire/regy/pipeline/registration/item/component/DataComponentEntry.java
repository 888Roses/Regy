package dev.rosenoire.regy.pipeline.registration.item.component;

import dev.rosenoire.regy.pipeline.registration.AbstractSimpleEntry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class DataComponentEntry<V> extends AbstractSimpleEntry<DataComponentType<V>> {
    public DataComponentEntry(@NonNull DataComponentType<V> value, @NonNull Identifier regyIdentifier, @NonNull Identifier identifier) {
        super(value, regyIdentifier, identifier);
    }

    public @Nullable V get(@NonNull ItemStack stack) {
        return stack.get(this.get());
    }

    public @Nullable V get(@NonNull Item item) {
        return this.get(item.getDefaultInstance());
    }

    public @NonNull ItemStack set(@NonNull ItemStack stack, V component) {
        stack.set(this.get(), component);
        return stack;
    }

    public @NonNull ItemStack setAndGet(@NonNull Item item, V component) {
        return this.set(item.getDefaultInstance(), component);
    }
}
