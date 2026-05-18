package dev.rosenoire.regy.pipeline.client.registration.sound.builder.impl;

import dev.rosenoire.regy.pipeline.client.registration.sound.ClientSoundEntryBuilder;
import dev.rosenoire.regy.pipeline.client.registration.sound.builder.AbstractSoundFileBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.ApiStatus;

@Environment(EnvType.CLIENT)
public class EventHolderSoundFileBuilder extends AbstractSoundFileBuilder<EventHolderSoundFileBuilder> {
    protected final Holder<SoundEvent> soundEvent;

    @ApiStatus.Internal
    public EventHolderSoundFileBuilder(ClientSoundEntryBuilder instance, Holder<SoundEvent> soundEvent) {
        super(instance);
        this.soundEvent = soundEvent;
    }

    @Override
    protected SoundTypeBuilder.EntryBuilder createEntry() {
        return SoundTypeBuilder.EntryBuilder.ofEvent(this.soundEvent);
    }
}