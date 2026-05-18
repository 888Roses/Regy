package dev.rosenoire.regy.pipeline.client.registration.sound.builder.impl;

import dev.rosenoire.regy.pipeline.client.registration.sound.ClientSoundEntryBuilder;
import dev.rosenoire.regy.pipeline.client.registration.sound.builder.AbstractSoundFileBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class FileSoundFileBuilder extends AbstractSoundFileBuilder<FileSoundFileBuilder> {
    protected final Identifier soundEvent;
    protected int variants;

    @ApiStatus.Internal
    public FileSoundFileBuilder(ClientSoundEntryBuilder instance, Identifier soundEvent) {
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
            this.addSoundEntry(transformCommon(sound));
        }

        return instance;
    }

    public FileSoundFileBuilder variants(int variants) {
        this.variants = variants;
        return self();
    }
}
