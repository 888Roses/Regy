package dev.rosenoire.regy.pipeline.datagen.impl.generator;

import dev.rosenoire.regy.api.text.StringHelper;
import dev.rosenoire.regy.pipeline.datagen.DataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public class LangDataGenerator extends FabricLanguageProvider implements DataGenerator {
    protected final TreeMap<String, String> entries = new TreeMap<>();

    public LangDataGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider registryLookup, TranslationBuilder translationBuilder) {
        entries.forEach(translationBuilder::add);
    }

    public void add(String key, String value) {
        if (StringHelper.isNullBlankOrEmpty(key) || StringHelper.isNullBlankOrEmpty(value)) {
            return;
        }

        this.entries.put(key, value);
    }

    @Override
    public @NonNull String getName() {
        return DataGenerators.LANG;
    }
}
