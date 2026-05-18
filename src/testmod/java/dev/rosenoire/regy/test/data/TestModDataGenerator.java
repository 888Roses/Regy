package dev.rosenoire.regy.test.data;

import dev.rosenoire.regy.test.common.TestModCommon;
import dev.rosenoire.regy.test.common.index.AllItems;
import dev.rosenoire.regy.test.common.index.AllSounds;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.sounds.SoundEvents;
import org.jspecify.annotations.NonNull;

import static dev.rosenoire.regy.test.client.TestModClient.REGY;

public class TestModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(@NonNull FabricDataGenerator generator) {
        items();
        sounds();

        TestModCommon.REGY.runDatagen(generator);
    }

    private void items() {
        REGY.item(AllItems.ELDRITCH_BLESSING)
                .tooltip(builder -> builder
                        .summary("Launches your home-grown vegetables at Enemies. Can be powered with _Air Pressure_ from a _Backtank_.")
                        .behaviour("When R-Clicked", "_Shoots_ a suitable item from your _Inventory_.")
                        .behaviour("While wearing Backtank", "_No Durability_ will be used. Instead, _Air Pressure_ is drained from the Tank.")
                )
                .register();
    }

    private void sounds() {
        REGY.sound(AllSounds.JUMP)
                .event(SoundEvents.ALLAY_DEATH).build()
                .register();
    }
}
