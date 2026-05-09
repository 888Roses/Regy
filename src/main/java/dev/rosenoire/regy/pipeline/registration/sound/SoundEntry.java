package dev.rosenoire.regy.pipeline.registration.sound;

import dev.rosenoire.regy.pipeline.registration.AbstractSimpleEntry;
import net.minecraft.sounds.SoundEvent;
import org.jspecify.annotations.NonNull;

public class SoundEntry extends AbstractSimpleEntry<SoundEvent> {
    public SoundEntry(@NonNull SoundEvent value) {
        super(value);
    }
}
