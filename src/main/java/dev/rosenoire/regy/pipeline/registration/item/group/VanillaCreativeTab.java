package dev.rosenoire.regy.pipeline.registration.item.group;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

/// Enumeration representing the Vanilla Minecraft [CreativeModeTab], and containing
/// their [ResourceKey] as well as display and saving utilities.
///
/// Contains a static [#LOOKUP] able to convert an input `String` representing the
/// [#getSerializedName()] of an instance of a [VanillaCreativeTab], into that vanilla
/// tab instance.
///
/// This enum class also contains a [#CODEC] to serialize its values.
public enum VanillaCreativeTab implements StringRepresentable {
    BUILDING_BLOCKS("building_blocks", CreativeModeTabs.BUILDING_BLOCKS),
    COLORED_BLOCKS("colored_blocks", CreativeModeTabs.COLORED_BLOCKS),
    NATURAL_BLOCKS("natural_blocks", CreativeModeTabs.NATURAL_BLOCKS),
    FUNCTIONAL_BLOCKS("functional_blocks", CreativeModeTabs.FUNCTIONAL_BLOCKS),
    REDSTONE_BLOCKS("redstone_blocks", CreativeModeTabs.REDSTONE_BLOCKS),
    HOTBAR("hotbar", CreativeModeTabs.HOTBAR),
    SEARCH("search", CreativeModeTabs.SEARCH),
    TOOLS_AND_UTILITIES("tools_and_utilities", CreativeModeTabs.TOOLS_AND_UTILITIES),
    COMBAT("combat", CreativeModeTabs.COMBAT),
    FOOD_AND_DRINKS("food_and_drinks", CreativeModeTabs.FOOD_AND_DRINKS),
    INGREDIENTS("ingredients", CreativeModeTabs.INGREDIENTS),
    SPAWN_EGGS("spawn_eggs", CreativeModeTabs.SPAWN_EGGS),
    OP_BLOCKS("op_blocks", CreativeModeTabs.OP_BLOCKS),
    INVENTORY("inventory", CreativeModeTabs.INVENTORY),
    ;

    public static final Codec<VanillaCreativeTab> CODEC = StringRepresentable.fromEnum(VanillaCreativeTab::values);
    public static final Function<String, @Nullable VanillaCreativeTab> LOOKUP = StringRepresentable.createNameLookup(values());

    private final @NonNull ResourceKey<CreativeModeTab> resourceKey;
    private final @NonNull String name;

    VanillaCreativeTab(@NonNull String name, @NonNull ResourceKey<CreativeModeTab> resourceKey) {
        this.resourceKey = resourceKey;
        this.name = name;
    }

    /// The [ResourceKey] of [CreativeModeTab] associated with that vanilla tab.
    public @NonNull ResourceKey<CreativeModeTab> resourceKey() {
        return resourceKey;
    }

    public @Override @NonNull String getSerializedName() {
        return this.name;
    }
}
