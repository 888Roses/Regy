package dev.rosenoire.regy.pipeline.registration.item.potion;

import dev.rosenoire.regy.api.text.NamingConventions;
import dev.rosenoire.regy.foundation.extensions.PotionExtension;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.datagen.DataGenObject;
import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.DataGenerators;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.LangDataGenerator;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.alchemy.Potion;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class PotionEntryBuilder<P> extends AbstractEntryBuilder<PotionEntry, P> implements DataGenObject {
    private final @NonNull String name;
    private final List<MobEffectInstance> statusEffects = new ArrayList<>();
    private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;

    private @Nullable String generatedName;

    public PotionEntryBuilder(@NonNull AbstractRegy<?> regy, P parent, String identifier, @NonNull String name) {
        super(regy, parent, identifier);
        this.name = name;
        simpleName();
    }

    @Override
    public @NonNull PotionEntry register() {
        var potion = new Potion(name, statusEffects.toArray(MobEffectInstance[]::new));
        var potionExtension = (PotionExtension) potion;
        potionExtension.regy$setRequiredFeaturesFlatSet(requiredFeatures);

        Registry.register(BuiltInRegistries.POTION, identifier(), potion);
        getRegy().dataGeneration().addData(this);

        return new PotionEntry(potion);
    }

    // region modifiers

    // region generic

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

    // region data generation

    public PotionEntryBuilder<P> name(@Nullable String lang) {
        this.generatedName = lang;
        return this;
    }

    public PotionEntryBuilder<P> simpleName() {
        this.generatedName = NamingConventions.HUMAN_TEXT.transform(path());
        return this;
    }

    public PotionEntryBuilder<P> customName() {
        this.generatedName = null;
        return this;
    }

    // endregion

    // endregion

    // region data generation

    // TODO: COLOUR IS CALCULATED USING THE EFFECTS CONTAINED BY THE
    //            POTION RATHER THAN BY THE POTION ITSELF WHICH MAKES SENSE LOL

    @Override
    public void collectDataGenProviders(DataGenProviderConsumer collector) {
        if (this.generatedName != null) {
            collector.addProvider(this::langDataGenProvider);
        }
    }

    private void langDataGenProvider(DataGeneration dataGeneration) {
        dataGeneration.<LangDataGenerator>getGeneratorOptional(DataGenerators.LANG).ifPresent(generator -> {
            generator.add("item.minecraft.potion.effect." + path(), "Potion of " + this.generatedName);
            generator.add("item.minecraft.splash_potion.effect." + path(), "Splash Potion of " + this.generatedName);
            generator.add("item.minecraft.lingering_potion.effect." + path(), "Lingering Potion of " + this.generatedName);
            generator.add("item.minecraft.tipped_arrow.effect." + path(), "Arrow of " + this.generatedName);
        });
    }

    // endregion
}
