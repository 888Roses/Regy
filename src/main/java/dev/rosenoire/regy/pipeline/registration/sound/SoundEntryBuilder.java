package dev.rosenoire.regy.pipeline.registration.sound;

import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class SoundEntryBuilder<P> extends AbstractEntryBuilder<SoundEntry, P> {
    protected boolean isFixedRange = false;
    protected float range = 0f;

    public SoundEntryBuilder(@NonNull AbstractRegy<?> regy, P parent, String identifier) {
        super(regy, parent, identifier);
        dynamicRange();
    }

    @Override
    public @NonNull SoundEntry register() {
        regy().log.info("Starting registration for sound '{}'...", identifier());

        regy().log.info("|---- Creating sound event...");
        var soundEvent = isFixedRange
                ? SoundEvent.createFixedRangeEvent(identifier(), range)
                : SoundEvent.createVariableRangeEvent(identifier());

        regy().log.info("|---- Registering...");
        Registry.register(BuiltInRegistries.SOUND_EVENT, identifier(), soundEvent);

        regy().log.info("|---- Creating Sound Entry...");
        var soundEntry = this.regy().entry(new SoundEntry(
                soundEvent,
                this.regyIdentifier(),
                this.identifier()
        ));

        regy().log.info("|-- Finished registration successfully!");
        return soundEntry;
    }

    @Override
    protected Identifier regyIdentifier() {
        return identifier().withPrefix("sound_event/");
    }

    // region modifiers

    // region generic

    public <B> B transform(Function<SoundEntryBuilder<P>, B> transformer) {
        return transformer.apply(this);
    }

    public SoundEntryBuilder<P> fixedRange(float range) {
        this.isFixedRange = true;
        this.range = range;
        return this;
    }

    public SoundEntryBuilder<P> dynamicRange() {
        this.isFixedRange = false;
        return this;
    }

    // endregion
}