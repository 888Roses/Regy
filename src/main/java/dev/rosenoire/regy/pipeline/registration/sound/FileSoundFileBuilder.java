package dev.rosenoire.regy.pipeline.registration.sound;

import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.minecraft.resources.Identifier;

public class FileSoundFileBuilder<P> extends AbstractSoundFileBuilder<FileSoundFileBuilder<P>, P> {
    protected final Identifier soundEvent;
    protected int variants;

    protected FileSoundFileBuilder(SoundEntryBuilder<P> instance, Identifier soundEvent) {
        super(instance);
        this.soundEvent = soundEvent;
    }

    @Override
    protected SoundTypeBuilder.EntryBuilder createEntry() {
        return SoundTypeBuilder.EntryBuilder.ofFile(this.soundEvent);
    }

    @Override
    public SoundEntryBuilder<P> build() {
        if (this.variants <= 0) {
            return super.build();
        }

        for (var i = 0; i < this.variants; i++) {
            var sound = SoundTypeBuilder.EntryBuilder.ofFile(this.soundEvent.withSuffix("_" + i));
            instance.soundEntries.add(transformCommon(sound));
        }

        return instance;
    }

    public FileSoundFileBuilder<P> variants(int variants) {
        this.variants = variants;
        return self();
    }
}
