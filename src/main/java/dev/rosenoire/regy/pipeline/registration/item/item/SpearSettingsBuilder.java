package dev.rosenoire.regy.pipeline.registration.item.item;

import dev.rosenoire.regy.api.MathUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwingAnimationType;
import net.minecraft.world.item.component.*;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

@SuppressWarnings("unused")
public class SpearSettingsBuilder<I extends Item, P> {
    private final ItemEntryBuilder<I, P> owner;

    private Properties<I, P> properties = Properties.defaultOf(this);
    private AdvancedProperties<I, P> advancedProperties = AdvancedProperties.defaultOf(this);

    public SpearSettingsBuilder(ItemEntryBuilder<I, P> owner) {
        this.owner = owner;
    }

    public Properties<I, P> properties() {
        return this.properties;
    }

    public AdvancedProperties<I, P> advancedProperties() {
        return this.advancedProperties;
    }

    public ItemEntryBuilder<I, P> build() {
        var value = owner
                .component(DataComponents.DAMAGE_TYPE, advancedProperties.damageType)
                .component(DataComponents.KINETIC_WEAPON, createKineticWeapon())
                .component(DataComponents.PIERCING_WEAPON, createPiercingWeapon())
                .component(DataComponents.ATTACK_RANGE, createAttackRange())
                .component(DataComponents.MINIMUM_ATTACK_CHARGE, advancedProperties.minimumAttackCharge)
                .component(DataComponents.SWING_ANIMATION, new SwingAnimation(SwingAnimationType.STAB, MathUtils.secs2ticks(properties.swingAnimationSeconds)));

        var material = value.material();
        if (material != null) {
            value = value.attackDamage(material.attackDamageBonus() + 1);
        }

        return value
                .attackSpeed(1f / properties.swingAnimationSeconds)
                .component(DataComponents.USE_EFFECTS, createUseEffects())
                .component(DataComponents.WEAPON, new Weapon(1));
    }

    private @NonNull UseEffects createUseEffects() {
        return new UseEffects(advancedProperties.canSprint, advancedProperties.interactWithVibrations, advancedProperties.speedMultiplier);
    }

    private @NonNull PiercingWeapon createPiercingWeapon() {
        return new PiercingWeapon(advancedProperties.dealsKnockback, advancedProperties.dismounts, Optional.of(properties.attackSound), Optional.of(properties.hitSound));
    }

    private @NonNull KineticWeapon createKineticWeapon() {
        return new KineticWeapon(
                advancedProperties.contactCooldownTicks,
                MathUtils.secs2ticks(properties.chargeDelaySeconds),
                KineticWeapon.Condition.ofAttackerSpeed(MathUtils.secs2ticks(properties.maxDurationForDismountSeconds), properties.minSpeedForDismount),
                KineticWeapon.Condition.ofAttackerSpeed(MathUtils.secs2ticks(properties.maxDurationForChargeKnockbackInSeconds), properties.minSpeedForChargeKnockback),
                KineticWeapon.Condition.ofRelativeSpeed(MathUtils.secs2ticks(properties.maxDurationForChargeDamageInSeconds), properties.minRelativeSpeedForChargeDamage),
                advancedProperties.forwardMovement,
                properties.chargeDamageMultiplier,
                Optional.of(properties.useSound),
                Optional.of(properties.hitSound)
        );
    }

    private @NonNull AttackRange createAttackRange() {
        return new AttackRange(
                advancedProperties.minRange,
                advancedProperties.maxRange,
                advancedProperties.minCreativeRange,
                advancedProperties.maxCreativeRange,
                advancedProperties.hitboxMargin,
                advancedProperties.mobFactor
        );
    }

    public static class Properties<I extends Item, P> {
        public static Properties<Item, ?> virtual() {
            return defaultOf(null);
        }

        public static <I extends Item, P> Properties<I, P> defaultOf(SpearSettingsBuilder<I, P> spear) {
            return new Properties<>(spear, spear == null);
        }

        private final SpearSettingsBuilder<I, P> owner;
        private final boolean isVirtual;

        private Holder<SoundEvent> useSound;
        private Holder<SoundEvent> hitSound;
        private Holder<SoundEvent> attackSound;
        private float swingAnimationSeconds;
        private float chargeDamageMultiplier;
        private float chargeDelaySeconds;
        private float maxDurationForDismountSeconds;
        private float minSpeedForDismount;
        private float maxDurationForChargeKnockbackInSeconds;
        private float minSpeedForChargeKnockback;
        private float maxDurationForChargeDamageInSeconds;
        private float minRelativeSpeedForChargeDamage;

        public Properties(SpearSettingsBuilder<I, P> owner, boolean isVirtual) {
            this.owner = owner;
            this.isVirtual = isVirtual;
        }

        public Properties<I, P> useSound(Holder<SoundEvent> useSound) {
            this.useSound = useSound;
            return this;
        }

        public Properties<I, P> hitSound(Holder<SoundEvent> hitSound) {
            this.hitSound = hitSound;
            return this;
        }

        public Properties<I, P> attackSound(Holder<SoundEvent> attackSound) {
            this.attackSound = attackSound;
            return this;
        }

        public Properties<I, P> swingAnimationSeconds(float swingAnimationSeconds) {
            this.swingAnimationSeconds = swingAnimationSeconds;
            return this;
        }

        public Properties<I, P> chargeDamageMultiplier(float chargeDamageMultiplier) {
            this.chargeDamageMultiplier = chargeDamageMultiplier;
            return this;
        }

        public Properties<I, P> chargeDelaySeconds(float chargeDelaySeconds) {
            this.chargeDelaySeconds = chargeDelaySeconds;
            return this;
        }

        public Properties<I, P> maxDurationForDismountSeconds(float maxDurationForDismountSeconds) {
            this.maxDurationForDismountSeconds = maxDurationForDismountSeconds;
            return this;
        }

        public Properties<I, P> minSpeedForDismount(float minSpeedForDismount) {
            this.minSpeedForDismount = minSpeedForDismount;
            return this;
        }

        public Properties<I, P> maxDurationForChargeKnockbackInSeconds(float maxDurationForChargeKnockbackInSeconds) {
            this.maxDurationForChargeKnockbackInSeconds = maxDurationForChargeKnockbackInSeconds;
            return this;
        }

        public Properties<I, P> minSpeedForChargeKnockback(float minSpeedForChargeKnockback) {
            this.minSpeedForChargeKnockback = minSpeedForChargeKnockback;
            return this;
        }

        public Properties<I, P> maxDurationForChargeDamageInSeconds(float maxDurationForChargeDamageInSeconds) {
            this.maxDurationForChargeDamageInSeconds = maxDurationForChargeDamageInSeconds;
            return this;
        }

        public Properties<I, P> minRelativeSpeedForChargeDamage(float minRelativeSpeedForChargeDamage) {
            this.minRelativeSpeedForChargeDamage = minRelativeSpeedForChargeDamage;
            return this;
        }

        public Properties<I, P> copy(Properties<?, ?> other) {
            return this
                    .useSound(other.useSound)
                    .hitSound(other.hitSound)
                    .attackSound(other.attackSound)
                    .swingAnimationSeconds(other.swingAnimationSeconds)
                    .chargeDamageMultiplier(other.chargeDamageMultiplier)
                    .chargeDelaySeconds(other.chargeDelaySeconds)
                    .maxDurationForDismountSeconds(other.maxDurationForDismountSeconds)
                    .minSpeedForDismount(other.minSpeedForDismount)
                    .maxDurationForChargeKnockbackInSeconds(other.maxDurationForChargeKnockbackInSeconds)
                    .minSpeedForChargeKnockback(other.minSpeedForChargeKnockback)
                    .maxDurationForChargeDamageInSeconds(other.maxDurationForChargeDamageInSeconds)
                    .minRelativeSpeedForChargeDamage(other.minRelativeSpeedForChargeDamage);
        }

        public SpearSettingsBuilder<I, P> build() {
            if (this.isVirtual) {
                return null;
            }

            this.owner.properties = this;
            return this.owner;
        }
    }

    public static class AdvancedProperties<I extends Item, P> {
        public static <I extends Item, P> AdvancedProperties<I, P> defaultOf(SpearSettingsBuilder<I, P> spear) {
            return new AdvancedProperties<>(spear)
                    .contactCooldownTicks(10)
                    .forwardMovement(0.38f)
                    .dealsKnockback(true)
                    .dismounts(false)
                    .minRange(2f)
                    .maxRange(4.5f)
                    .minCreativeRange(2f)
                    .maxCreativeRange(6.5f)
                    .hitboxMargin(0.125f)
                    .mobFactor(0.5f)
                    .minimumAttackCharge(1f)
                    .canSprint(true)
                    .interactWithVibrations(false)
                    .speedMultiplier(1f);
        }

        private final SpearSettingsBuilder<I, P> owner;

        private int contactCooldownTicks;
        private float forwardMovement;
        private boolean dealsKnockback;
        private boolean dismounts;
        private float minRange;
        private float maxRange;
        private float minCreativeRange;
        private float maxCreativeRange;
        private float hitboxMargin;
        private float mobFactor;
        private float minimumAttackCharge;
        private boolean canSprint;
        private boolean interactWithVibrations;
        private float speedMultiplier;
        private EitherHolder<DamageType> damageType = new EitherHolder<>(DamageTypes.SPEAR);

        public AdvancedProperties(SpearSettingsBuilder<I, P> owner) {
            this.owner = owner;
        }

        public AdvancedProperties<I, P> contactCooldownTicks(int contactCooldownTicks) {
            this.contactCooldownTicks = contactCooldownTicks;
            return this;
        }

        public AdvancedProperties<I, P> forwardMovement(float forwardMovement) {
            this.forwardMovement = forwardMovement;
            return this;
        }

        public AdvancedProperties<I, P> dealsKnockback(boolean dealsKnockback) {
            this.dealsKnockback = dealsKnockback;
            return this;
        }

        public AdvancedProperties<I, P> dismounts(boolean dismounts) {
            this.dismounts = dismounts;
            return this;
        }

        public AdvancedProperties<I, P> minRange(float minRange) {
            this.minRange = minRange;
            return this;
        }

        public AdvancedProperties<I, P> maxRange(float maxRange) {
            this.maxRange = maxRange;
            return this;
        }

        public AdvancedProperties<I, P> minCreativeRange(float minCreativeRange) {
            this.minCreativeRange = minCreativeRange;
            return this;
        }

        public AdvancedProperties<I, P> maxCreativeRange(float maxCreativeRange) {
            this.maxCreativeRange = maxCreativeRange;
            return this;
        }

        public AdvancedProperties<I, P> hitboxMargin(float hitboxMargin) {
            this.hitboxMargin = hitboxMargin;
            return this;
        }

        public AdvancedProperties<I, P> mobFactor(float mobFactor) {
            this.mobFactor = mobFactor;
            return this;
        }

        public AdvancedProperties<I, P> minimumAttackCharge(float minimumAttackCharge) {
            this.minimumAttackCharge = minimumAttackCharge;
            return this;
        }

        public AdvancedProperties<I, P> canSprint(boolean canSprint) {
            this.canSprint = canSprint;
            return this;
        }

        public AdvancedProperties<I, P> interactWithVibrations(boolean interactWithVibrations) {
            this.interactWithVibrations = interactWithVibrations;
            return this;
        }

        public AdvancedProperties<I, P> speedMultiplier(float speedMultiplier) {
            this.speedMultiplier = speedMultiplier;
            return this;
        }

        public AdvancedProperties<I, P> damageType(EitherHolder<DamageType> damageType) {
            this.damageType = damageType;
            return this;
        }

        public AdvancedProperties<I, P> copy(AdvancedProperties<?, ?> other) {
            return this
                    .contactCooldownTicks(other.contactCooldownTicks)
                    .forwardMovement(other.forwardMovement)
                    .dealsKnockback(other.dealsKnockback)
                    .dismounts(other.dismounts)
                    .minRange(other.minRange)
                    .maxRange(other.maxRange)
                    .minCreativeRange(other.minCreativeRange)
                    .maxCreativeRange(other.maxCreativeRange)
                    .hitboxMargin(other.hitboxMargin)
                    .mobFactor(other.mobFactor)
                    .minimumAttackCharge(other.minimumAttackCharge)
                    .canSprint(other.canSprint)
                    .interactWithVibrations(other.interactWithVibrations)
                    .speedMultiplier(other.speedMultiplier)
                    .damageType(other.damageType);
        }

        public SpearSettingsBuilder<I, P> build() {
            this.owner.advancedProperties = this;
            return this.owner;
        }
    }
}
