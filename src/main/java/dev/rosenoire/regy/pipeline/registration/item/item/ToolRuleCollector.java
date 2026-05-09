package dev.rosenoire.regy.pipeline.registration.item.item;

import net.minecraft.core.HolderGetter;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

/// `FunctionalInterface` responsible for collecting an arbitrary amount [Tool.Rule] using
/// the given block lookup.
public @FunctionalInterface interface ToolRuleCollector {
    /// Collects an arbitrary amount of [Tool.Rule] while giving access to a block lookup
    /// in the form of the `lookup` [HolderGetter].
    /// @param lookup `NonNull` [HolderGetter] of [Block] giving access to the user to the block registry.
    /// @param collector [Consumer] of [Tool.Rule] collecting the rules.
    void collect(
            @NonNull HolderGetter<Block> lookup,
            @NonNull Consumer<Tool.@NonNull Rule> collector
    );
}