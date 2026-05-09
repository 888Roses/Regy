package dev.rosenoire.regy.pipeline.registration.sound;

import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public class EventHolderSoundFileBuilder<P> extends AbstractSoundFileBuilder<EventHolderSoundFileBuilder<P>, P> {
    protected final Holder<SoundEvent> soundEvent;

    protected EventHolderSoundFileBuilder(SoundEntryBuilder<P> instance, Holder<SoundEvent> soundEvent) {
        super(instance);
        this.soundEvent = soundEvent;
    }

    @Override
    protected SoundTypeBuilder.EntryBuilder createEntry() {
        return SoundTypeBuilder.EntryBuilder.ofEvent(this.soundEvent);
    }
}