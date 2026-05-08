package dev.rosenoire.regy.pipeline.registration.item.group;

import dev.rosenoire.regy.api.data.DataUtils;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class CreativeTabMapper {
    public final AbstractRegy<?> owner;
    public final Map<CreativeModeTab, ArrayList<ItemEntry<? extends Item>>> map = new HashMap<>();
    protected @Nullable CreativeModeTab mainTab;

    public CreativeTabMapper(AbstractRegy<?> owner) {
        this.owner = owner;
    }

    public void initializeEvents() {
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register(this::modifyEntriesAll);

        for (VanillaCreativeTab vanillaTab : VanillaCreativeTab.values()) {
            ItemGroupEvents.modifyEntriesEvent(vanillaTab.resourceKey())
                    .register(entries -> this.modifyVanillaTab(vanillaTab, entries));
        }
    }

    public void setMainTab(@NonNull CreativeModeTab tab) {
        this.mainTab = tab;
    }

    public <I extends Item> void add(CreativeModeTab tab, ItemEntry<I> entry) {
        DataUtils.addToMapLister(map, ArrayList::new, tab, entry);
    }

    public void modifyEntriesAll(CreativeModeTab creativeModeTab, FabricItemGroupEntries fabricItemGroupEntries) {
        map.computeIfPresent(creativeModeTab, (tab, entries) -> {
            entries.forEach(entry -> {
                fabricItemGroupEntries.accept(entry.get());
            });

            return entries;
        });

        synchronized (this.owner) {
            this.owner.entries().stream()
                    .map(entry -> entry instanceof ItemEntry<? extends Item> itemEntry ? itemEntry : null)
                    .filter(Objects::nonNull)
                    .filter(itemEntry -> {
                        if (!itemEntry.tabGroup.showInMainTab()) return false;
                        if (mainTab != null && mainTab.equals(creativeModeTab)) {
                            fabricItemGroupEntries.accept(itemEntry.get());
                        }

                        return !itemEntry.tabGroup.vanillaTabs().isEmpty() || !itemEntry.tabGroup.tabs().isEmpty();
                    })
                    .filter(itemEntry -> itemEntry.tabGroup.tabs().contains(creativeModeTab))
                    .forEach(itemEntry -> fabricItemGroupEntries.accept(itemEntry.get()));
        }
    }

    private void modifyVanillaTab(VanillaCreativeTab vanillaTab, FabricItemGroupEntries entries) {
        synchronized (this.owner) {
            this.owner.entries().stream()
                    .map(entry -> entry instanceof ItemEntry<? extends Item> itemEntry ? itemEntry : null)
                    .filter(Objects::nonNull)
                    .filter(itemEntry -> !itemEntry.tabGroup.vanillaTabs().isEmpty() || !itemEntry.tabGroup.tabs().isEmpty())
                    .filter(itemEntry -> itemEntry.tabGroup.vanillaTabs().contains(vanillaTab))
                    .forEach(itemEntry -> entries.accept(itemEntry.get()));
        }
    }
}
