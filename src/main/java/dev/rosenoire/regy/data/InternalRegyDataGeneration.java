package dev.rosenoire.regy.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jspecify.annotations.NonNull;

import static dev.rosenoire.regy.client.InternalRegyClient.INTERNAL_REGY;

public class InternalRegyDataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(@NonNull FabricDataGenerator fabricDataGenerator) {
        // INTERNAL_REGY.setupDatagen(fabricDataGenerator);
        // INTERNAL_REGY.runDatagen();
    }
}
