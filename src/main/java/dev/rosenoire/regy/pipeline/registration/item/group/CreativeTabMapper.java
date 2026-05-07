package dev.rosenoire.regy.pipeline.registration.item.group;

import dev.rosenoire.regy.api.data.DataUtils;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CreativeTabMapper {
    public final AbstractRegy<?> owner;
    public final Map<CreativeModeTab, ArrayList<ItemEntry<? extends Item>>> map = new HashMap<>();
    protected @Nullable CreativeModeTab mainTab;

    public CreativeTabMapper(AbstractRegy<?> owner) {
        this.owner = owner;
    }

    public void setMainTab(@NonNull CreativeModeTab tab) {
        this.mainTab = tab;
    }

    public <I extends Item> void add(CreativeModeTab tab, ItemEntry<I> entry) {
        DataUtils.addToMapLister(map, ArrayList::new, tab, entry);
    }

    public void modifyEntriesAll(CreativeModeTab creativeModeTab, FabricItemGroupEntries fabricItemGroupEntries) {
        var addedItems = new HashSet<Item>();

        map.computeIfPresent(creativeModeTab, (tab, entries) -> {
            entries.forEach(entry -> {
                var item = entry.get().asItem();
                fabricItemGroupEntries.accept(item);
                addedItems.add(item);
            });

            return entries;
        });

        if (mainTab == null) {
            return;
        }

        if (creativeModeTab.equals(mainTab)) {
            synchronized (this.owner) {
                this.owner.entries.stream()
                        .filter(entry -> entry instanceof ItemEntry<? extends Item>)
                        .map(entry -> (ItemEntry<? extends Item>) entry)
                        .map(ItemEntry::get)
                        .filter(item -> !addedItems.contains(item))
                        .forEach(fabricItemGroupEntries::accept);
            }
        }
    }
}
