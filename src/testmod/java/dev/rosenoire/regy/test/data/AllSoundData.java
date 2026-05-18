package dev.rosenoire.regy.test.data;

import dev.rosenoire.regy.test.common.index.AllSounds;
import net.minecraft.sounds.SoundEvents;

import static dev.rosenoire.regy.test.client.TestModClient.REGY;

public interface AllSoundData {
    static void init() {
        REGY.sound(AllSounds.JUMP)
                .event(SoundEvents.ALLAY_DEATH).build()
                .register();
    }
}
