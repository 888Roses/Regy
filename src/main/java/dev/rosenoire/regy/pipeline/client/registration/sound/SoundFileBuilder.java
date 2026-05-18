package dev.rosenoire.regy.pipeline.client.registration.sound;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public interface SoundFileBuilder {
    ClientSoundEntryBuilder build();

    static FileSoundFileBuilder ofFile(ClientSoundEntryBuilder instance, Identifier identifier) {
        return new FileSoundFileBuilder(instance, identifier);
    }

    static EventSoundFileBuilder ofEvent(ClientSoundEntryBuilder instance, SoundEvent soundEvent) {
        return new EventSoundFileBuilder(instance, soundEvent);
    }

    static EventHolderSoundFileBuilder ofEvent(ClientSoundEntryBuilder instance, Holder<SoundEvent> soundEvent) {
        return new EventHolderSoundFileBuilder(instance, soundEvent);
    }
}