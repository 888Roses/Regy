package dev.rosenoire.regy.client.index;

import dev.rosenoire.regy.client.model.item.properties.select.ItemSkinProperty;
import dev.rosenoire.regy.pipeline.client.registration.item.model.properties.select.SelectItemModelPropertyEntry;
import org.jetbrains.annotations.ApiStatus;

import static dev.rosenoire.regy.client.InternalRegyClient.INTERNAL_REGY;

@ApiStatus.NonExtendable
@ApiStatus.Internal
public interface InternalConditionalItemModelProperties {
    SelectItemModelPropertyEntry ITEM_SKIN = INTERNAL_REGY
            .selectItemModelProperty("item_skin")
            .key(ItemSkinProperty.TYPE)
            .register();

    @ApiStatus.Internal
    static void register() {
    }
}
