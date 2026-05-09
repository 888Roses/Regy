package dev.rosenoire.regy.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import dev.rosenoire.regy.foundation.callbacks.ResourceKeyAwareCreativeTabCallbacks;
import dev.rosenoire.regy.foundation.callbacks.ResourceKeyAwareCreativeTabCallbacksImpl;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CreativeModeTab.class, priority = 1500)
public class CreativeModeTabMixinSquared {
    @TargetHandler(
            mixin = "net.fabricmc.fabric.mixin.itemgroup.CreativeModeTabMixin",
            name = "getStacks"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/fabricmc/fabric/api/itemgroup/v1/ItemGroupEvents$ModifyEntriesAll;modifyEntries(Lnet/minecraft/world/item/CreativeModeTab;Lnet/fabricmc/fabric/api/itemgroup/v1/FabricItemGroupEntries;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void getStacks$invokeCreativeTabMapper(
            CreativeModeTab.ItemDisplayParameters context,
            CallbackInfo ci,
            CallbackInfo ci1,
            @Local CreativeModeTab self,
            @Local ResourceKey<CreativeModeTab> registryKey,
            @Local FabricItemGroupEntries entries
    ) {
        var modifyEntriesEvent = ResourceKeyAwareCreativeTabCallbacksImpl.getModifyEntriesEvent(registryKey);
        if (modifyEntriesEvent != null) {
            modifyEntriesEvent.invoker().modifyEntries(self, registryKey, entries);
        }

        ResourceKeyAwareCreativeTabCallbacks.MODIFY_ENTRIES_ALL.invoker().modifyEntries(self, registryKey, entries);
    }
}
