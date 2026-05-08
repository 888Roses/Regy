package dev.rosenoire.regy.pipeline.registration.item.group;

import net.minecraft.world.item.CreativeModeTab;

import java.util.HashSet;

public record CreativeTabGroup(HashSet<CreativeModeTab> tabs, HashSet<VanillaCreativeTab> vanillaTabs, boolean showInMainTab) {
    public static class Builder {
        private final HashSet<CreativeModeTab> tabs = new HashSet<>();
        private final HashSet<VanillaCreativeTab> vanillaTabs = new HashSet<>();
        private boolean showInMainTab = true;

        public void showInMainTab(boolean inMainTab) {
            this.showInMainTab = inMainTab;
        }

        public void addTab(CreativeModeTab tab) {
            if (tab != null) {
                tabs.add(tab);
            }
        }

        public void addTab(VanillaCreativeTab tab) {
            if (tab != null) {
                vanillaTabs.add(tab);
            }
        }

        public CreativeTabGroup build() {
            return new CreativeTabGroup(this.tabs, this.vanillaTabs, this.showInMainTab);
        }
    }
}
