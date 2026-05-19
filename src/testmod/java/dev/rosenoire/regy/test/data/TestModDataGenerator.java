package dev.rosenoire.regy.test.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jspecify.annotations.NonNull;

import static dev.rosenoire.regy.test.client.TestModClient.REGY;

public class TestModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(@NonNull FabricDataGenerator fabricDataGenerator) {
        REGY.setupDatagen(fabricDataGenerator);

        AllItemData.init();
        // AllBlockData.init();
        AllSoundData.init();

        REGY.runDatagen();
    }
}
