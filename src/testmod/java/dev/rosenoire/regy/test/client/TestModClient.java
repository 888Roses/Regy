package dev.rosenoire.regy.test.client;

import dev.rosenoire.regy.pipeline.Regy;
import dev.rosenoire.regy.pipeline.client.ClientRegy;
import dev.rosenoire.regy.test.common.TestModCommon;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;

public class TestModClient implements ClientModInitializer {
    public static final ClientRegy<Regy> REGY = ClientRegy.create(TestModCommon.REGY);

    @Override
    public void onInitializeClient() {
        REGY.initializeEvents();

        ClientTickEvents.END_CLIENT_TICK.register(GridConstrainedItemHandler::tick);
        WorldRenderEvents.BEFORE_ENTITIES.register(GridConstrainedItemHandler::render);
    }
}
