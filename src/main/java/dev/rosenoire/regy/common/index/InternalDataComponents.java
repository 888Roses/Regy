package dev.rosenoire.regy.common.index;

import dev.rosenoire.regy.common.content.datacomponent.ItemSkin;
import dev.rosenoire.regy.pipeline.registration.item.component.DataComponentEntry;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.ApiStatus;

import static dev.rosenoire.regy.common.InternalRegyCommon.INTERNAL_REGY;

@ApiStatus.NonExtendable
@ApiStatus.Internal
public interface InternalDataComponents {
    /// Stores information about the skin currently stored by the [Item] holding this component.
    DataComponentEntry<ItemSkin> ITEM_SKIN = INTERNAL_REGY
            .<ItemSkin>component("item_skin")
            .codec(ItemSkin.CODEC)
            .streamCodec(ItemSkin.STREAM_CODEC)
            .register();

    @ApiStatus.Internal
    static void register() {
    }
}
