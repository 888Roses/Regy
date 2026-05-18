package dev.rosenoire.regy.pipeline.registration.sound;

import dev.rosenoire.regy.api.logging.LogEntry;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.registration.AbstractEntryBuilder;
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
        var log = LogEntry.of(this);
        log.info("|> §bold§cyan(SoundEntryBuilder)§end §green\"{}\"§end", this.identifier());

        if (this.isFixedRange) {
            log.info(
                    "|  > Creating fixed range SoundEvent... §blue({}b)§end",
                    Math.round(this.range * 100f) / 100f
            );
        } else {
            log.info("|  > Creating variable range SoundEvent...");
        }

        var soundEvent = this.isFixedRange
                ? SoundEvent.createFixedRangeEvent(this.identifier(), this.range)
                : SoundEvent.createVariableRangeEvent(this.identifier());

        log.info("|  > Registering SoundEvent...");
        Registry.register(BuiltInRegistries.SOUND_EVENT, this.identifier(), soundEvent);

        log.info("|  > Creating SoundEntry...");
        var soundEntry = this.regy().entry(new SoundEntry(
                soundEvent,
                this.regyIdentifier(),
                this.identifier()
        ));

        log.send();
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