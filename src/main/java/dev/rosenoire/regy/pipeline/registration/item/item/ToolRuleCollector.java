package dev.rosenoire.regy.pipeline.registration.item.item;

import net.minecraft.core.HolderGetter;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

@FunctionalInterface
public interface ToolRuleCollector {
    void collect(HolderGetter<Block> lookup, Consumer<Tool.Rule> collector);
}
