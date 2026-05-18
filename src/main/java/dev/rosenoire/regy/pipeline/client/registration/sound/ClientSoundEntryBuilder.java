package dev.rosenoire.regy.pipeline.client.registration.sound;

import dev.rosenoire.regy.api.text.NamingConventions;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.client.registration.AbstractClientEntryBuilder;
import dev.rosenoire.regy.pipeline.datagen.DataGenObject;
import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.DataGenerators;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.LangDataGenerator;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.SoundDataGenerator;
import dev.rosenoire.regy.pipeline.registration.sound.SoundEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Util;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class ClientSoundEntryBuilder extends AbstractClientEntryBuilder<SoundEntry, SoundEvent> implements DataGenObject {
    protected boolean isFixedRange = false;
    protected float range = 0f;
    protected @Nullable String subtitle = null;
    protected boolean replace = false;
    final List<SoundTypeBuilder.EntryBuilder> soundEntries = new ArrayList<>();

    public ClientSoundEntryBuilder(@NonNull AbstractRegy<?> regy, @NonNull SoundEntry soundEntry) {
        super(regy, soundEntry);

        this.simpleSubtitle();
    }

    public ClientSoundEntryBuilder replace(boolean replace) {
        this.replace = replace;
        return this;
    }

    // endregion

    // region data generation

    public ClientSoundEntryBuilder subtitle(@NonNull String lang) {
        this.subtitle = lang;
        return this;
    }

    public ClientSoundEntryBuilder simpleSubtitle() {
        var raw = this.identifier().getPath().replaceAll("\\./", "_");
        var lang = NamingConventions.SENTENCE.transform(raw);
        return this.subtitle(lang);
    }

    public ClientSoundEntryBuilder noSubtitle() {
        this.subtitle = null;
        return this;
    }

    public FileSoundFileBuilder file(Identifier identifier) {
        return SoundFileBuilder.ofFile(this, identifier);
    }

    public FileSoundFileBuilder file(String identifier) {
        return file(regy().id(identifier));
    }

    public EventSoundFileBuilder event(SoundEvent soundEvent) {
        return SoundFileBuilder.ofEvent(this, soundEvent);
    }

    public EventHolderSoundFileBuilder event(Holder<SoundEvent> soundEvent) {
        return SoundFileBuilder.ofEvent(this, soundEvent);
    }

    // endregion

    // endregion

    // region data generation

    @Override
    public void collectDataGenProviders(DataGenProviderConsumer collector) {
        collector.addProvider(this::dataGenSoundProvider);
    }

    private void dataGenSoundProvider(DataGeneration dataGeneration) {
        dataGeneration
                .<SoundDataGenerator>getGeneratorOptional(DataGenerators.SOUNDS)
                .ifPresent(generator -> generator.add(this::exportSoundDatagen));

        dataGeneration
                .<LangDataGenerator>getGeneratorOptional(DataGenerators.LANG)
                .ifPresent(this::exportSoundLang);
    }

    private void exportSoundLang(LangDataGenerator generator) {
        if (subtitle == null) {
            return;
        }

        var descriptionId = Util.makeDescriptionId("subtitles", this.identifier());
        generator.add(descriptionId, subtitle);
    }

    private void exportSoundDatagen(HolderLookup.@NonNull Provider registryLookup, FabricSoundsProvider.@NonNull SoundExporter exporter) {
        var builder = SoundTypeBuilder.of().replace(this.replace);

        synchronized (this.soundEntries) {
            this.soundEntries.forEach(builder::sound);
        }

        exporter.add(identifier(), builder);
    }

    // endregion
}