package dev.rosenoire.regy.pipeline.registration.sound;

import dev.rosenoire.regy.api.text.NamingConventions;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.datagen.DataGenObject;
import dev.rosenoire.regy.pipeline.datagen.DataGeneration;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.DataGenerators;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.LangDataGenerator;
import dev.rosenoire.regy.pipeline.datagen.impl.generator.SoundDataGenerator;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Util;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class SoundEntryBuilder<P> extends AbstractEntryBuilder<SoundEntry, P> implements DataGenObject {
    protected boolean isFixedRange = false;
    protected float range = 0f;
    protected @Nullable String subtitle = null;
    protected boolean replace = false;
    final List<SoundTypeBuilder.EntryBuilder> soundEntries = new ArrayList<>();

    public SoundEntryBuilder(@NonNull AbstractRegy<?> regy, P parent, String identifier) {
        super(regy, parent, identifier);
        simpleSubtitle();
        dynamicRange();
    }

    @Override
    public @NonNull SoundEntry register() {
        var soundEvent = isFixedRange
                ? SoundEvent.createFixedRangeEvent(identifier(), range)
                : SoundEvent.createVariableRangeEvent(identifier());

        Registry.register(BuiltInRegistries.SOUND_EVENT, identifier(), soundEvent);

        // TODO: Automatize!
        getRegy().dataGeneration().addData(this);
        return new SoundEntry(soundEvent);
    }

    // region modifiers

    // region generic

    public <B> B transform(Function<SoundEntryBuilder<P>, B> transformer) {
        return transformer.apply(this);
    }

    public SoundEntryBuilder<P> fixedRange(float range) {
        this.isFixedRange = true;
        this.range = range;
        return this;
    }

    public SoundEntryBuilder<P> dynamicRange() {
        this.isFixedRange = false;
        return this;
    }

    public SoundEntryBuilder<P> replace(boolean replace) {
        this.replace = replace;
        return this;
    }

    // endregion

    // region data generation

    public SoundEntryBuilder<P> subtitle(@NonNull String lang) {
        this.subtitle = lang;
        return this;
    }

    public SoundEntryBuilder<P> simpleSubtitle() {
        var raw = this.identifier().getPath().replaceAll("\\./", "_");
        var lang = NamingConventions.SENTENCE.transform(raw);
        return this.subtitle(lang);
    }

    public SoundEntryBuilder<P> noSubtitle() {
        this.subtitle = null;
        return this;
    }

    public FileSoundFileBuilder<P> file(Identifier identifier) {
        return SoundFileBuilder.ofFile(this, identifier);
    }

    public FileSoundFileBuilder<P> file(String identifier) {
        return file(getRegy().id(identifier));
    }

    public EventSoundFileBuilder<P> event(SoundEvent soundEvent) {
        return SoundFileBuilder.ofEvent(this, soundEvent);
    }

    public EventHolderSoundFileBuilder<P> event(Holder<SoundEvent> soundEvent) {
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