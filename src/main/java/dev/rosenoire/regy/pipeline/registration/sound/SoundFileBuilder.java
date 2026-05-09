package dev.rosenoire.regy.pipeline.registration.sound;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public interface SoundFileBuilder<P> {
    SoundEntryBuilder<P> build();

    static <P> FileSoundFileBuilder<P> ofFile(SoundEntryBuilder<P> instance, Identifier identifier) {
        return new FileSoundFileBuilder<>(instance, identifier);
    }

    static <P> EventSoundFileBuilder<P> ofEvent(SoundEntryBuilder<P> instance, SoundEvent soundEvent) {
        return new EventSoundFileBuilder<>(instance, soundEvent);
    }

    static <P> EventHolderSoundFileBuilder<P> ofEvent(SoundEntryBuilder<P> instance, Holder<SoundEvent> soundEvent) {
        return new EventHolderSoundFileBuilder<>(instance, soundEvent);
    }
}