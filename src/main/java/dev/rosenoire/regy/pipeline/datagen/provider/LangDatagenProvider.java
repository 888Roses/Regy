package dev.rosenoire.regy.pipeline.datagen.provider;

import com.google.gson.JsonObject;
import dev.rosenoire.regy.common.RegyCommon;
import dev.rosenoire.regy.pipeline.Regy;
import dev.rosenoire.regy.pipeline.datagen.ProviderContext;
import dev.rosenoire.regy.pipeline.datagen.ProviderType;
import dev.rosenoire.regy.pipeline.datagen.filter.DatagenTranslatable;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public class LangDatagenProvider extends AbstractDatagenProvider {
    // TODO: Implement additional language support.
    private final String languageCode = "en_us";

    public LangDatagenProvider(ProviderContext providerContext) {
        super(providerContext);
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput writer) {
        var translationEntries = new TreeMap<String, String>();

        return this.ctx.registryLookup().thenCompose(lookup -> {
            ctx.owner().datagenTargets.stream()
                    .filter(target -> target instanceof DatagenTranslatable)
                    .map(target -> (DatagenTranslatable) target)
                    .map(DatagenTranslatable::getDatagenTranslation)
                    .forEach(trans -> translationEntries.put(trans.key(), trans.value()));

            var json = new JsonObject();

            for (var entry : translationEntries.entrySet()) {
                RegyCommon.log.info("Generating entry - \"{}\" : \"{}\"", entry.getKey(), entry.getValue());
                json.addProperty(entry.getKey(), entry.getValue());
            }

            return DataProvider.saveStable(writer, json, langFilePath(this.languageCode));
        });
    }

    private Path langFilePath(String languageCode) {
        return ctx.dataOutput()
                .createPathProvider(PackOutput.Target.RESOURCE_PACK, "lang")
                .json(Identifier.fromNamespaceAndPath(ctx.dataOutput().getModId(), languageCode));
    }

    @Override
    public @NonNull String getName() {
        return ProviderType.LANG.getSerializedName();
    }
}
