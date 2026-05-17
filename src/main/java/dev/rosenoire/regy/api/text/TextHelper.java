package dev.rosenoire.regy.api.text;

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
}
