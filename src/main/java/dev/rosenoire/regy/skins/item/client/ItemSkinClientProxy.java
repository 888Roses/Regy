package dev.rosenoire.regy.skins.item.client;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public interface ItemSkinClientProxy {
    static void register() {
        ItemTooltipCallback.EVENT.register(ItemSkinTooltip::display);
    }
}
