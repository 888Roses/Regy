package dev.rosenoire.regy.pipeline.registration.item.group;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NonNull;

import java.util.Collection;

// Unused
@ApiStatus.Experimental
public class CreativeTabDisplayer implements CreativeModeTab.Output {
    protected final @NonNull CreativeModeTab creativeTab;
    protected final @NonNull FeatureFlagSet featureFlagSet;

    protected final Collection<ItemStack> contents = ItemStackLinkedSet.createTypeAndComponentsSet();
    protected final Collection<ItemStack> searchContents = ItemStackLinkedSet.createTypeAndComponentsSet();

    public CreativeTabDisplayer(
            @NonNull CreativeModeTab creativeTab,
            @NonNull FeatureFlagSet featureFlagSet
    ) {
        this.creativeTab = creativeTab;
        this.featureFlagSet = featureFlagSet;
    }

    protected boolean shouldAddToContents(CreativeModeTab.@NonNull TabVisibility visibility) {
        return visibility == CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS ||
                visibility == CreativeModeTab.TabVisibility.PARENT_TAB_ONLY;
    }

    protected boolean shouldAddToSearch(CreativeModeTab.@NonNull TabVisibility visibility) {
        return visibility == CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS ||
                visibility == CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY;
    }

    protected boolean isDuplicateStack(
            @NonNull ItemStack itemStack,
            CreativeModeTab.@NonNull TabVisibility visibility
    ) {
        return this.contents.contains(itemStack) && visibility != CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY;
    }

    @SuppressWarnings("ExtractMethodRecommender")
    @Override
    public void accept(
            @NonNull ItemStack itemStack,
            CreativeModeTab.@NonNull TabVisibility visibility
    ) {
        if (!itemStack.isItemEnabled(this.featureFlagSet)) {
            return;
        }

        var itemStackName = itemStack.getDisplayName().getString();
        var creativeTabName = this.creativeTab.getDisplayName().getString();

        if (itemStack.getCount() != 1) {
            throw new IllegalStateException(
                    "Detected ItemStack "
                            + itemStackName
                            + " with count "
                            + itemStack.getCount() +
                            " added to CreativeModeTab "
                            + creativeTabName
                            + ". ItemStack count should be exactly 1!"
            );
        }

        if (this.isDuplicateStack(itemStack, visibility)) {
            throw new IllegalStateException(
                    "Detected duplicate ItemStacks "
                            + itemStackName
                            + " added to CreativeModeTab "
                            + creativeTabName
                            + ". This is not allowed!"
            );
        }

        if (this.shouldAddToSearch(visibility)) this.searchContents.add(itemStack);
        if (this.shouldAddToContents(visibility)) this.contents.add(itemStack);
    }
}
