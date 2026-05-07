package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.pipeline.registration.item.group.CreativeTabEntry;

import static dev.rosenoire.regy.test.common.TestModCommon.REGY;

public interface AllCreativeTabs {
    CreativeTabEntry MAIN = REGY.tab("main")
            .name("Eldritch Testimony")
            .icon(AllItems.ELDRITCH_BLESSING)
            .mainTab()
            .register();

    static void register() {
    }
}

