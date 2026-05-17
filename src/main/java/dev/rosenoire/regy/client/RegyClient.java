package dev.rosenoire.regy.client;

import dev.rosenoire.regy.tooltips.TooltipProxy;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class RegyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register(TooltipProxy::itemTooltip);
    }
}
