package dev.rosenoire.regy.pipeline.registration.sound;

import com.mojang.datafixers.types.Func;
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;

import java.util.function.Function;

public abstract class AbstractSoundFileBuilder<F extends AbstractSoundFileBuilder<F, P>, P> implements SoundFileBuilder<P> {
    protected final SoundEntryBuilder<P> instance;
    protected float volume = 1f;
    protected float pitch = 1f;
    protected int attenuationDistance = 16;
    protected boolean stream = false;
    protected boolean preload = false;

    protected AbstractSoundFileBuilder(SoundEntryBuilder<P> instance) {
        this.instance = instance;
    }

    protected abstract SoundTypeBuilder.EntryBuilder createEntry();

    protected SoundTypeBuilder.EntryBuilder transformCommon(SoundTypeBuilder.EntryBuilder builder) {
        return builder
                .volume(volume)
                .pitch(pitch)
                .attenuationDistance(attenuationDistance)
                .stream(stream)
                .preload(stream);
    }

    public SoundEntryBuilder<P> build() {
        instance.soundEntries.add(transformCommon(createEntry()));
        return instance;
    }

    public F self() {
        //noinspection unchecked
        return (F) this;
    }

    public <B> B transform(Function<F, B> transformer) {
        return transformer.apply(self());
    }

    public F volume(float volume) {
        this.volume = volume;
        return self();
    }

    public F pitch(float pitch) {
        this.pitch = pitch;
        return self();
    }

    public F attenuationDistance(int attenuationDistance) {
        this.attenuationDistance = attenuationDistance;
        return self();
    }

    public F stream(boolean stream) {
        this.stream = stream;
        return self();
    }

    public F preload(boolean preload) {
        this.preload = preload;
        return self();
    }
}
