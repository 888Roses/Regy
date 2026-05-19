package dev.rosenoire.regy.api.text;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;

public interface TextHelper {
    static MutableComponent formattedToComponent(FormattedCharSequence sequence) {
        final var component = Component.empty();
        sequence.accept((index, style, codepoint) -> {
            component.append(Component.literal(Character.toString(codepoint)).setStyle(style));
            return true;
        });

        return component;
    }

    static boolean exists(String translationKey) {
        return I18n.exists(translationKey);
    }
}
