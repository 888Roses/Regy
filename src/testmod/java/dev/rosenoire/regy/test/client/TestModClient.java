package dev.rosenoire.regy.test.client;

import dev.rosenoire.regy.common.RegyCommon;
import dev.rosenoire.regy.pipeline.Regy;
import dev.rosenoire.regy.pipeline.client.ClientRegy;
import dev.rosenoire.regy.test.common.TestModCommon;
import net.fabricmc.api.ClientModInitializer;

public class TestModClient implements ClientModInitializer {
    public static final ClientRegy<Regy> REGY = ClientRegy.create(TestModCommon.REGY);

    @Override
    public void onInitializeClient() {
        RegyCommon.log.info("Initializing Client!");
        REGY.initializeEvents();
    }
}
