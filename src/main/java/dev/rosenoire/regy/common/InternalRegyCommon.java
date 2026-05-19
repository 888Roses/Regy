package dev.rosenoire.regy.common;

import dev.rosenoire.regy.common.index.InternalBlockTags;
import dev.rosenoire.regy.common.index.InternalDataComponents;
import dev.rosenoire.regy.pipeline.Regy;
import dev.rosenoire.regy.skins.item.ItemSkinProxy;
import net.fabricmc.api.ModInitializer;

public class InternalRegyCommon implements ModInitializer {
    public static final String MOD_ID = "regy";
    public static final Regy INTERNAL_REGY = Regy.create(MOD_ID);

    @Override
    public void onInitialize() {
        INTERNAL_REGY.initializeEvents();

        InternalDataComponents.register();
        InternalBlockTags.register();

        ItemSkinProxy.register();
    }
}
