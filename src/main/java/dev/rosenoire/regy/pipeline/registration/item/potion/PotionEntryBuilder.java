package dev.rosenoire.regy.pipeline.registration.item.potion;

import dev.rosenoire.regy.foundation.extensions.PotionExtension;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.alchemy.Potion;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class PotionEntryBuilder<P> extends AbstractEntryBuilder<PotionEntry, P> {
    private final @NonNull String name;
    private final List<MobEffectInstance> statusEffects = new ArrayList<>();
    private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;

    public PotionEntryBuilder(@NonNull AbstractRegy<?> regy, P parent, String identifier, @NonNull String name) {
        super(regy, parent, identifier);
        this.name = name;
    }

    @Override
    public @NonNull PotionEntry register() {
        regy().log.info("Starting registration for potion '{}'...", identifier());

        regy().log.info("|---- Creating potion and registering...");
        var potion = new Potion(name, statusEffects.toArray(MobEffectInstance[]::new));
        ((PotionExtension) potion).regy$setRequiredFeaturesFlatSet(requiredFeatures);
        Registry.register(BuiltInRegistries.POTION, identifier(), potion);

        regy().log.info("|---- Creating Potion Entry...");
        var potionEntry = new PotionEntry(potion, this.regyIdentifier(), this.identifier());
        potionEntry = this.regy().entry(potionEntry);

        regy().log.info("|-- Finished registration successfully!");
        return potionEntry;
    }

    @Override
    protected Identifier regyIdentifier() {
        return identifier().withPrefix("potion/");
    }

    // region modifiers

    public <B> B transform(Function<PotionEntryBuilder<P>, B> transformer) {
        return transformer.apply(this);
    }

    public PotionEntryBuilder<P> requiredFeatures(FeatureFlag... feature) {
        this.requiredFeatures = FeatureFlags.REGISTRY.subset(feature);
        return this;
    }

    public PotionEntryBuilder<P> effect(MobEffectInstance instance) {
        this.statusEffects.add(instance);
        return this;
    }

    public PotionEffectBuilder<P> effect(Holder<MobEffect> effect) {
        return new PotionEffectBuilder<>(this, effect);
    }

    // endregion
}
