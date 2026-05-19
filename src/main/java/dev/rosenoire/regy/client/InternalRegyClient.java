package dev.rosenoire.regy.client;

import dev.rosenoire.regy.client.index.InternalConditionalItemModelProperties;
import dev.rosenoire.regy.common.InternalRegyCommon;
import dev.rosenoire.regy.pipeline.Regy;
import dev.rosenoire.regy.pipeline.client.ClientRegy;
import dev.rosenoire.regy.skins.item.client.ItemSkinClientProxy;
import dev.rosenoire.regy.tooltips.TooltipProxy;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class InternalRegyClient implements ClientModInitializer {
    public static final ClientRegy<Regy> INTERNAL_REGY = ClientRegy.create(InternalRegyCommon.INTERNAL_REGY);

    @Override
    public void onInitializeClient() {
        INTERNAL_REGY.initializeEvents();

        InternalConditionalItemModelProperties.register();

        ItemTooltipCallback.EVENT.register(TooltipProxy::itemTooltip);
        ItemSkinClientProxy.register();
    }
}
