package dev.rosenoire.regy.pipeline.client.registration.item.potion;

import dev.rosenoire.regy.api.text.NamingConventions;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.client.registration.AbstractClientEntryBuilder;
import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.DataGenerators;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.LangDataGenerator;
import dev.rosenoire.regy.pipeline.registration.item.potion.PotionEntry;
import net.minecraft.world.item.alchemy.Potion;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class ClientPotionEntryBuilder extends AbstractClientEntryBuilder<PotionEntry, Potion> {
    private @Nullable String generatedName;

    public ClientPotionEntryBuilder(@NonNull AbstractRegy<?> regy, @NonNull PotionEntry entry) {
        super(regy, entry);
        this.simpleName();
    }

    @Override
    public void collectDataGenProviders(DataGenProviderConsumer collector) {
        if (this.generatedName != null) {
            collector.addProvider(this::langDataGenProvider);
        }
    }

    private void langDataGenProvider(DataGeneration dataGeneration) {
        dataGeneration.<LangDataGenerator>getGeneratorOptional(DataGenerators.LANG).ifPresent(generator -> {
            generator.add("item.minecraft.potion.effect." + this.path(), "Potion of " + this.generatedName);
            generator.add("item.minecraft.splash_potion.effect." + this.path(), "Splash Potion of " + this.generatedName);
            generator.add("item.minecraft.lingering_potion.effect." + this.path(), "Lingering Potion of " + this.generatedName);
            generator.add("item.minecraft.tipped_arrow.effect." + this.path(), "Arrow of " + this.generatedName);
        });
    }

    public ClientPotionEntryBuilder name(@Nullable String lang) {
        this.generatedName = lang;
        return this;
    }

    public ClientPotionEntryBuilder simpleName() {
        this.generatedName = NamingConventions.HUMAN_TEXT.transform(this.path());
        return this;
    }

    public ClientPotionEntryBuilder customName() {
        this.generatedName = null;
        return this;
    }
}
