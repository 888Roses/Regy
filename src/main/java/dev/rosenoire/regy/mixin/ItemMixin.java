package dev.rosenoire.regy.mixin;

import dev.rosenoire.regy.api.text.RoseText;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.function.Consumer;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(at = @At("HEAD"), method = "appendHoverText")
    private void appendHoverText$drawTags(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (!tooltipFlag.isAdvanced()) {
            return;
        }

        var tags = itemStack.getTags().toList();
        if (tags.isEmpty()) return;

        RoseText.text("Tags:").styled(ChatFormatting.GRAY).consume(consumer);

        tags.stream()
                .sorted(Comparator.comparing(TagKey::location))
                .forEach(value -> RoseText
                        .text("#" + value.location())
                        .styled(ChatFormatting.LIGHT_PURPLE)
                        .indent(2)
                        .consume(consumer)
                );
    }
}
