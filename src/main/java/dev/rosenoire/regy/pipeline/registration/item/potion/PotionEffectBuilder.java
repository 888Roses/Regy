package dev.rosenoire.regy.pipeline.registration.item.potion;

import dev.rosenoire.regy.pipeline.content.alchemy.MobEffectBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.jspecify.annotations.NonNull;

@SuppressWarnings("unused")
public class PotionEffectBuilder<P> {
    private final PotionEntryBuilder<P> instance;
    private final MobEffectBuilder builder;

    public PotionEffectBuilder(PotionEntryBuilder<P> instance, Holder<MobEffect> effect) {
        this.instance = instance;
        this.builder = MobEffectBuilder.of(effect);
    }

    public PotionEffectBuilder<P> duration(int ticks) {
        this.builder.duration(ticks);
        return this;
    }

    public PotionEffectBuilder<P> durationSeconds(float seconds) {
        this.builder.durationSeconds(seconds);
        return this;
    }

    public PotionEffectBuilder<P> amplifier(int amplifier) {
        this.builder.amplifier(amplifier);
        return this;
    }

    public PotionEffectBuilder<P> ambient(boolean ambient) {
        this.builder.ambient(ambient);
        return this;
    }

    public PotionEffectBuilder<P> visible() {
        this.builder.visible();
        return this;
    }

    public PotionEffectBuilder<P> invisible() {
        this.builder.invisible();
        return this;
    }

    public PotionEffectBuilder<P> showIcon() {
        this.builder.showIcon();
        return this;
    }

    public PotionEffectBuilder<P> hideIcon() {
        this.builder.hideIcon();
        return this;
    }

    public PotionEffectBuilder<P> hiddenEffect(@NonNull MobEffectInstance hiddenEffect) {
        this.builder.hiddenEffect(hiddenEffect);
        return this;
    }

    public PotionEntryBuilder<P> build() {
        return instance.effect(this.builder.build());
    }
}