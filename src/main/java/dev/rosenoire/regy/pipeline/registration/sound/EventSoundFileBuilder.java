package dev.rosenoire.regy.pipeline.registration.sound;

import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.minecraft.sounds.SoundEvent;

public class EventSoundFileBuilder<P> extends AbstractSoundFileBuilder<EventSoundFileBuilder<P>, P> {
    protected final SoundEvent soundEvent;

    protected EventSoundFileBuilder(SoundEntryBuilder<P> instance, SoundEvent soundEvent) {
        super(instance);
        this.soundEvent = soundEvent;
    }

    @Override
    protected SoundTypeBuilder.EntryBuilder createEntry() {
        return SoundTypeBuilder.EntryBuilder.ofEvent(this.soundEvent);
    }
}
