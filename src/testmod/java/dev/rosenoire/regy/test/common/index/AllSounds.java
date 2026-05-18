package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.pipeline.registration.sound.*;

import static dev.rosenoire.regy.test.common.TestModCommon.REGY;

public interface AllSounds {
    SoundEntry JUMP = REGY.sound("jump").register();

    static void register() {
    }
}
