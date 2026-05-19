package dev.rosenoire.regy.skins.item;

import dev.rosenoire.regy.common.index.InternalBlockTags;
import dev.rosenoire.regy.common.index.InternalDataComponents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;

public interface ItemSkinProxy {
    static void register() {
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            var itemStack = player.getItemInHand(hand);
            var component = InternalDataComponents.ITEM_SKIN.get(itemStack);

            var pos = hitResult.getBlockPos();
            var blockState = level.getBlockState(pos);
            if (component != null && InternalBlockTags.SKIN_SWAPPER.is(blockState)) {
                var mutable = component.mutable();

                if (player.isCrouching()) {
                    mutable.decrementIndex();
                } else {
                    mutable.incrementIndex();
                }

                InternalDataComponents.ITEM_SKIN.set(itemStack, mutable.build());
                player.playSound(SoundEvents.SMITHING_TABLE_USE, 1, 1);

                if (player instanceof ServerPlayer serverPlayer) {
                    var center = pos.getCenter();
                    var radius = 0.5;
                    serverPlayer.level().sendParticles(
                            serverPlayer,
                            ParticleTypes.HAPPY_VILLAGER,
                            false, true,
                            center.x, center.y, center.z,
                            20,
                            radius, radius, radius,
                            0
                    );
                }

                return InteractionResult.SUCCESS;
            }

            return InteractionResult.PASS;
        });
    }
}
