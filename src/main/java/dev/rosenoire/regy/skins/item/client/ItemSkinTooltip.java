package dev.rosenoire.regy.skins.item.client;

import dev.rosenoire.regy.api.text.Palette;
import dev.rosenoire.regy.api.text.RoseText;
import dev.rosenoire.regy.api.text.TextHelper;
import dev.rosenoire.regy.common.index.InternalDataComponents;
import dev.rosenoire.regy.tooltips.TooltipPalette;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemSkinTooltip {
    public static void display(
            ItemStack itemStack,
            Item.TooltipContext tooltipContext,
            TooltipFlag tooltipFlag,
            List<Component> tooltip
    ) {
        var item = itemStack.getItem();
        var component = InternalDataComponents.ITEM_SKIN.get(itemStack);

        if (component == null) {
            return;
        }

        var palette = TooltipPalette.getPalette(item);
        var skin = component.skins().get(component.index());

        RoseText.trans(item.getDescriptionId() + ".skin." + skin)
                .highlighted(palette.body())
                .forTooltip(tooltip);

        var authorKey = item.getDescriptionId() + ".skin." + skin + ".author";

        if (TextHelper.exists(authorKey)) {
            RoseText.trans(authorKey)
                    .normal(palette.body())
                    .prependLast("by ")
                    .normal(palette.body())
                    .forTooltip(tooltip);
        } else if (component.index() != 0) {
            RoseText.trans("item.regy.skin.unknown.author")
                    .normal(palette.body())
                    .prependLast("by ")
                    .normal(palette.body())
                    .forTooltip(tooltip);
        }
    }
}
