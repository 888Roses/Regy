package dev.rosenoire.regy.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class Internal_RegyLangProvider extends FabricLanguageProvider {
    public Internal_RegyLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider lookup, @NonNull TranslationBuilder translation) {
        translation.add("tooltip.regy.show_tooltip", "Hold [_Shift_] to learn more");
    }
}
