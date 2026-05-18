package dev.rosenoire.regy.test.common;

import dev.rosenoire.regy.api.logging.LogEntry;
import dev.rosenoire.regy.pipeline.Regy;
import dev.rosenoire.regy.test.common.index.*;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestModCommon implements ModInitializer {
    public static final String MOD_ID = "regy_test_mod";
    public static final Regy REGY = Regy.create(MOD_ID);

    @Override
    public void onInitialize() {
        REGY.initializeEvents();

        AllItems.register();
        AllMaterials.register();
        AllCreativeTabs.register();
        AllSounds.register();
        AllPotions.register();
        AllBlocks.register();
        AllItemTags.register();
        AllDataComponentTypes.register();
    }
}
