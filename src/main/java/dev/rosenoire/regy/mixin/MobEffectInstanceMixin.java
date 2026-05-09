package dev.rosenoire.regy.mixin;

import dev.rosenoire.regy.foundation.extensions.MobEffectInstanceExtension;
import net.minecraft.world.effect.MobEffectInstance;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobEffectInstance.class)
public class MobEffectInstanceMixin implements MobEffectInstanceExtension {
    @Shadow private @Nullable MobEffectInstance hiddenEffect;

    @Override
    public @Nullable MobEffectInstance regy$getHiddenEffect() {
        return this.hiddenEffect;
    }
}
