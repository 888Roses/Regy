package dev.rosenoire.regy.pipeline.content.alchemy;

import dev.rosenoire.regy.api.MathUtils;
import dev.rosenoire.regy.foundation.extensions.MobEffectInstanceExtension;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class MobEffectBuilder {
    protected final Holder<MobEffect> effect;
    protected int duration = 20;
    protected int amplifier = 0;
    protected boolean ambient = false;
    protected boolean visible = true;
    protected boolean showIcon = true;
    protected @Nullable MobEffectInstance hiddenEffect = null;

    public static MobEffectBuilder of(Holder<MobEffect> effect) {
        return new MobEffectBuilder(effect);
    }

    public static MobEffectBuilder of(MobEffectInstance instance) {
        return of(instance.getEffect()).copy(instance);
    }

    protected MobEffectBuilder(Holder<MobEffect> effect) {
        this.effect = effect;
    }

    public MobEffectBuilder copy(MobEffectInstance instance) {
        this.duration(instance.getDuration());
        this.amplifier(instance.getAmplifier() + 1);
        this.ambient(instance.isAmbient());
        this.visible = instance.isVisible();
        this.showIcon = instance.showIcon();
        this.hiddenEffect = ((MobEffectInstanceExtension) instance).regy$getHiddenEffect();
        return this;
    }

    public MobEffectBuilder duration(int ticks) {
        this.duration = ticks;
        return this;
    }

    public MobEffectBuilder durationSeconds(float seconds) {
        return duration(MathUtils.secs2ticks(seconds));
    }

    public MobEffectBuilder amplifier(int amplifier) {
        this.amplifier = amplifier;
        return this;
    }

    public MobEffectBuilder ambient(boolean ambient) {
        this.ambient = ambient;
        return this;
    }

    public MobEffectBuilder visible() {
        this.visible = true;
        return this;
    }

    public MobEffectBuilder invisible() {
        this.visible = false;
        return this;
    }

    public MobEffectBuilder showIcon() {
        this.showIcon = true;
        return this;
    }

    public MobEffectBuilder hideIcon() {
        this.showIcon = false;
        return this;
    }

    public MobEffectBuilder hiddenEffect(@NonNull MobEffectInstance hiddenEffect) {
        this.hiddenEffect = hiddenEffect;
        return this;
    }

    public MobEffectInstance build() {
        return new MobEffectInstance(effect, duration, amplifier - 1, ambient, visible, showIcon, hiddenEffect);
    }
}
