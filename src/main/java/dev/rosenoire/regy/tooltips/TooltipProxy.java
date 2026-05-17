package dev.rosenoire.regy.tooltips;

import dev.rosenoire.regy.api.text.Palette;
import dev.rosenoire.regy.api.text.RoseText;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.RegyInternal;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public interface TooltipProxy {
    int MAX_BEHAVIOURS = 2147483647;
    int MAX_WIDTH = 190;

    static void itemTooltip(
            ItemStack itemStack,
            Item.TooltipContext ignoredTooltipContext,
            TooltipFlag ignoredTooltipFlag,
            List<Component> tooltip
    ) {
        var item = itemStack.getItem();
        var desc = item.getDescriptionId();

        var sumId = desc + ".tooltip.summary";

        if (isEmpty(sumId)) {
            return;
        }

        var client = Minecraft.getInstance();
        var showTooltip = client.hasShiftDown();
        var palette = getPalette(item);

        RoseText.trans("tooltip.regy.show_tooltip")
                .palette(palette.getHeaderPalette(showTooltip))
                .forTooltip(tooltip);

        if (!showTooltip) {
            return;
        }

        RoseText.empty().forTooltip(tooltip);

        RoseText.trans(sumId)
                .palette(palette.body())
                .wrap(MAX_WIDTH)
                .forTooltip(tooltip);

        int behaviourIndex = 1;

        while (behaviourIndex < MAX_BEHAVIOURS) {
            var behaviourId = desc + ".tooltip.behaviour" + behaviourIndex;
            var conditionId = desc + ".tooltip.condition" + behaviourIndex;

            if (isEmpty(behaviourId) || isEmpty(conditionId)) {
                break;
            }

            if (behaviourIndex == 1) {
                RoseText.empty().forTooltip(tooltip);
            }

            RoseText.trans(conditionId)
                    .palette(palette.condition())
                    .wrap(MAX_WIDTH)
                    .forTooltip(tooltip);

            RoseText.trans(behaviourId)
                    .palette(palette.behaviour())
                    .wrap(MAX_WIDTH)
                    .indent(2)
                    .forTooltip(tooltip);

            behaviourIndex++;
        }
    }

    private static boolean isEmpty(@NonNull String translationKey) {
        return !I18n.exists(translationKey);
    }

    @SuppressWarnings("deprecation")
    private static Optional<AbstractRegy<?>> getOwner(Item item) {
        return RegyInternal.regyFromReference(item.builtInRegistryHolder());
    }

    private static TooltipPalette getPalette(Item item) {
        return getOwner(item)
                .map(AbstractRegy::tooltipPalette)
                .orElse(TooltipPalette.DEFAULT);
    }

    private static Palette getShowTooltipPalette(@NonNull Palette palette, boolean showTooltip) {
        return !showTooltip ? palette : new Palette(
                component -> component.withStyle(ChatFormatting.WHITE),
                palette.normalStyle()
        );
    }
}
