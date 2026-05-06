package dev.rosenoire.regy.test.data;

import dev.rosenoire.regy.test.common.TestModCommon;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jspecify.annotations.NonNull;

public class TestModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(@NonNull FabricDataGenerator fabricDataGenerator) {
        TestModCommon.REGY.setupDatagen(fabricDataGenerator.createPack());
    }
}
