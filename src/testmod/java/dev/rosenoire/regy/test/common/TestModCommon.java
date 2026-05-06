package dev.rosenoire.regy.test.common;

import dev.rosenoire.regy.pipeline.Regy;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestModCommon implements ModInitializer {
    public static final String MOD_ID = "regy_test_mod";
    public static final Logger log = LoggerFactory.getLogger(MOD_ID);

    public static final Regy REGY = Regy.create(MOD_ID);

    @Override
    public void onInitialize() {
        log.info("Initialized " + MOD_ID);

        REGY
                .item("skibidi", Item::new)
                .register();
    }
}
