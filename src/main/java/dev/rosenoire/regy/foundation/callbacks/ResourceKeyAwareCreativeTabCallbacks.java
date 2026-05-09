package dev.rosenoire.regy.foundation.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

// TODO: Documentation!
public class ResourceKeyAwareCreativeTabCallbacks {
    private ResourceKeyAwareCreativeTabCallbacks() {
    }

    public static final Event<ModifyEntriesAll> MODIFY_ENTRIES_ALL = EventFactory.createArrayBacked(ModifyEntriesAll.class, callbacks -> (group, key, entries) -> {
        for (ModifyEntriesAll callback : callbacks) {
            callback.modifyEntries(group, key, entries);
        }
    });

    public static Event<ModifyEntries> modifyEntriesEvent(ResourceKey<CreativeModeTab> registryKey) {
        return ResourceKeyAwareCreativeTabCallbacksImpl.getOrCreateModifyEntriesEvent(registryKey);
    }

    @FunctionalInterface
    public interface ModifyEntries {
        void modifyEntries(CreativeModeTab group, ResourceKey<CreativeModeTab> registryKey, FabricItemGroupEntries entries);
    }

    @FunctionalInterface
    public interface ModifyEntriesAll {
        void modifyEntries(CreativeModeTab group, ResourceKey<CreativeModeTab> registryKey, FabricItemGroupEntries entries);
    }
}

