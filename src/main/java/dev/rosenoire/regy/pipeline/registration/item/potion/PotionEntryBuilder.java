package dev.rosenoire.regy.pipeline.registration.item.potion;

import dev.rosenoire.regy.api.logging.LogEntry;
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
        var log = LogEntry.of(this);

        log.info("|> §bold§cyan(PotionEntryBuilder)§end §green\"{}\"§end", this.identifier());

        log.info("|  > Creating Potion...");
        var potion = new Potion(name, statusEffects.toArray(MobEffectInstance[]::new));

        log.info("|  > Setting requiredFeaturesFlagSet...");
        ((PotionExtension) potion).regy$setRequiredFeaturesFlagSet(requiredFeatures);

        log.info("|  > Registering Potion...");
        Registry.register(BuiltInRegistries.POTION, identifier(), potion);

        log.info("|  > Creating PotionEntry...");
        var potionEntry = this.regy().entry(new PotionEntry(
                potion,
                this.regyIdentifier(),
                this.identifier()
        ));

        log.send();
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
