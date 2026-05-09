package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.pipeline.registration.sound.*;
import net.minecraft.sounds.SoundEvents;

import static dev.rosenoire.regy.test.common.TestModCommon.REGY;

public interface AllSounds {
    SoundEntry JUMP = REGY
            .sound("jump")
            .event(SoundEvents.ALLAY_DEATH).build()
            .register();

    static void register() {
    }
}
