package dev.rosenoire.regy.pipeline.client.registration.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class FileSoundFileBuilder extends AbstractSoundFileBuilder<FileSoundFileBuilder> {
    protected final Identifier soundEvent;
    protected int variants;

    protected FileSoundFileBuilder(ClientSoundEntryBuilder instance, Identifier soundEvent) {
        super(instance);
        this.soundEvent = soundEvent;
    }

    @Override
    protected SoundTypeBuilder.EntryBuilder createEntry() {
        return SoundTypeBuilder.EntryBuilder.ofFile(this.soundEvent);
    }

    @Override
    public ClientSoundEntryBuilder build() {
        if (this.variants <= 0) {
            return super.build();
        }

        for (var i = 0; i < this.variants; i++) {
            var sound = SoundTypeBuilder.EntryBuilder.ofFile(this.soundEvent.withSuffix("_" + i));
            instance.soundEntries.add(transformCommon(sound));
        }

        return instance;
    }

    public FileSoundFileBuilder variants(int variants) {
        this.variants = variants;
        return self();
    }
}
