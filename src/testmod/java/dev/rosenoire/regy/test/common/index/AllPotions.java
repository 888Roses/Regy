package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.pipeline.registration.item.potion.PotionEntry;
import net.minecraft.world.effect.MobEffects;

import static dev.rosenoire.regy.test.common.TestModCommon.REGY;

public interface AllPotions {
    PotionEntry SIGMA = REGY
            .potion("sigma", "sigma")
            .effect(MobEffects.STRENGTH).durationSeconds(3f).amplifier(1).visible().build()
            .register();

    static void register() {
    }
}
