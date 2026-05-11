package dev.rosenoire.regy.pipeline.registration.item.group;

import dev.rosenoire.regy.api.data.DataUtils;
import dev.rosenoire.regy.common.RegyCommon;
import dev.rosenoire.regy.foundation.callbacks.ResourceKeyAwareCreativeTabCallbacks;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.registration.Entry;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;

// TODO: Documentation!
public class CreativeTabMapper {
    public final AbstractRegy<?> owner;
    protected @Nullable CreativeModeTab mainTab;

    public CreativeTabMapper(AbstractRegy<?> owner) {
        this.owner = owner;
    }

    // TODO: Documentation!
    public void initializeEvents() {
        ResourceKeyAwareCreativeTabCallbacks.MODIFY_ENTRIES_ALL.register(this::modifyEntriesAll);
    }

    // TODO: Documentation!
    public void setMainTab(@NonNull CreativeModeTab tab) {
        this.mainTab = tab;
    }

    // TODO: Documentation!
    public boolean hasMainTab() {
        return this.mainTab != null;
    }

    // TODO: Documentation!
    public boolean isMainTab(CreativeModeTab creativeModeTab) {
        return hasMainTab() && creativeModeTab.equals(this.mainTab);
    }

    // TODO: Documentation!
    private void modifyEntriesAll(CreativeModeTab tab, ResourceKey<CreativeModeTab> resourceKey, FabricItemGroupEntries entries) {
        var allEntries = this.owner.entries();
        var isMainTab = isMainTab(tab);
        synchronized (allEntries) {
            allEntries
                    .stream()
                    .map(entry -> entry instanceof ItemEntry<? extends Item> itemEntry ? itemEntry : null)
                    .filter(Objects::nonNull)
                    // First sort by name to make sure they are always in a consistent order.
                    .sorted(Comparator.comparing(i -> i.get().getClass().getSimpleName()))
                    .sorted(Comparator.comparing(i -> i.get().getName().getString()))
                    .forEach(entry -> {
                        var item = entry.get();
                        var group = entry.tabGroup;

                        if (isMainTab && group.showInMainTab()) {
                            entries.accept(item);
                            // we continue to the next tab after adding the item since this is the
                            // main tab, additional checks make no sense.
                            return;
                        }

                        // In the case where:
                        // - We're in the main tab (can only be in the main tab at this point if the
                        //   looked at entry isn't shown in it since we'd have continued earlier
                        //   otherwise).
                        // - Or if the group isn't in any tab.
                        // We can just skip the search all together for optimisation.
                        if (isMainTab || (group.tabs().isEmpty() && group.tabResourceKeys().isEmpty())) {
                            return;
                        }

                        if (group.tabs().contains(tab) || group.tabResourceKeys().contains(resourceKey)) {
                            entries.accept(item);
                        }
                    });
        }
    }
}