package dev.rosenoire.regy.foundation.extensions;

import net.minecraft.world.effect.MobEffectInstance;
import org.jspecify.annotations.Nullable;

public interface MobEffectInstanceExtension {
    @Nullable MobEffectInstance regy$getHiddenEffect();
}
