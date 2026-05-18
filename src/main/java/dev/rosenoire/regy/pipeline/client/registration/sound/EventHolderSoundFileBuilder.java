package dev.rosenoire.regy.pipeline.client.registration.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

@Environment(EnvType.CLIENT)
public class EventHolderSoundFileBuilder extends AbstractSoundFileBuilder<EventHolderSoundFileBuilder> {
    protected final Holder<SoundEvent> soundEvent;

    protected EventHolderSoundFileBuilder(ClientSoundEntryBuilder instance, Holder<SoundEvent> soundEvent) {
        super(instance);
        this.soundEvent = soundEvent;
    }

    @Override
    protected SoundTypeBuilder.EntryBuilder createEntry() {
        return SoundTypeBuilder.EntryBuilder.ofEvent(this.soundEvent);
    }
}