package dev.rosenoire.regy.pipeline.client.registration.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.minecraft.sounds.SoundEvent;

@Environment(EnvType.CLIENT)
public class EventSoundFileBuilder extends AbstractSoundFileBuilder<EventSoundFileBuilder> {
    protected final SoundEvent soundEvent;

    protected EventSoundFileBuilder(ClientSoundEntryBuilder instance, SoundEvent soundEvent) {
        super(instance);
        this.soundEvent = soundEvent;
    }

    @Override
    protected SoundTypeBuilder.EntryBuilder createEntry() {
        return SoundTypeBuilder.EntryBuilder.ofEvent(this.soundEvent);
    }
}
