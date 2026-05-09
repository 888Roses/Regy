package dev.rosenoire.regy.pipeline.registration.item.group;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;

// TODO: Documentation!
public record CreativeTabGroup(HashSet<CreativeModeTab> tabs, HashSet<ResourceKey<CreativeModeTab>> tabResourceKeys, boolean showInMainTab) {
    // TODO: Documentation!
    public static class Builder {
        private final HashSet<CreativeModeTab> tabs = new HashSet<>();
        private final HashSet<ResourceKey<CreativeModeTab>> tabResourceKeys = new HashSet<>();
        private boolean showInMainTab = true;

        // TODO: Documentation!
        public void showInMainTab(boolean display) {
            this.showInMainTab = display;
        }

        // TODO: Documentation!
        public void addTab(@Nullable CreativeModeTab tab) {
            if (tab == null) {
                return;
            }

            tabs.add(tab);
        }

        // TODO: Documentation!
        public void addTab(@Nullable VanillaCreativeTab tab) {
            if (tab == null) {
                return;
            }

            addTab(tab.resourceKey());
        }

        // TODO: Documentation!
        public void addTab(@Nullable ResourceKey<CreativeModeTab> resourceKey) {
            if (resourceKey == null) {
                return;
            }

            tabResourceKeys.add(resourceKey);
        }

        // TODO: Documentation!
        public CreativeTabGroup build() {
            return new CreativeTabGroup(this.tabs, this.tabResourceKeys, this.showInMainTab);
        }
    }
}
