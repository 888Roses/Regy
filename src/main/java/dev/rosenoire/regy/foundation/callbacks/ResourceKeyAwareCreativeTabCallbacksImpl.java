package dev.rosenoire.regy.foundation.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ResourceKeyAwareCreativeTabCallbacksImpl {
    private static final Map<ResourceKey<CreativeModeTab>, Event<ResourceKeyAwareCreativeTabCallbacks.ModifyEntries>> ITEM_GROUP_EVENT_MAP = new HashMap<>();

    public static Event<ResourceKeyAwareCreativeTabCallbacks.ModifyEntries> getOrCreateModifyEntriesEvent(ResourceKey<CreativeModeTab> registryKey) {
        return ITEM_GROUP_EVENT_MAP.computeIfAbsent(registryKey, (g -> createModifyEvent()));
    }

    @Nullable
    public static Event<ResourceKeyAwareCreativeTabCallbacks.ModifyEntries> getModifyEntriesEvent(ResourceKey<CreativeModeTab> registryKey) {
        return ITEM_GROUP_EVENT_MAP.get(registryKey);
    }

    private static Event<ResourceKeyAwareCreativeTabCallbacks.ModifyEntries> createModifyEvent() {
        return EventFactory.createArrayBacked(ResourceKeyAwareCreativeTabCallbacks.ModifyEntries.class, callbacks -> (tab, key, entries) -> {
            for (ResourceKeyAwareCreativeTabCallbacks.ModifyEntries callback : callbacks) {
                callback.modifyEntries(tab, key, entries);
            }
        });
    }

}
