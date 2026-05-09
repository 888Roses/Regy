package dev.rosenoire.regy.mixin;

import dev.rosenoire.regy.foundation.extensions.PotionExtension;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.alchemy.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Potion.class)
public class PotionMixin implements PotionExtension {
    @Shadow private FeatureFlagSet requiredFeatures;

    @Override
    public void regy$setRequiredFeaturesFlatSet(FeatureFlagSet set) {
        this.requiredFeatures = set;
    }
}
