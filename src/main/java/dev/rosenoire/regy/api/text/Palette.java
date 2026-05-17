package dev.rosenoire.regy.api.text;

import dev.rosenoire.regy.api.data.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jspecify.annotations.NonNull;

import java.util.function.UnaryOperator;

public record Palette(
        @NonNull UnaryOperator<@NonNull MutableComponent> highlightStyle,
        @NonNull UnaryOperator<@NonNull MutableComponent> normalStyle
) {
    public static final Palette DEFAULT = styled(ChatFormatting.GRAY, ChatFormatting.DARK_GRAY);

    public static Palette styled(@NonNull Style highlightStyle, @NonNull Style normalStyle) {
        return new Palette(
                component -> component.withStyle(highlightStyle),
                component -> component.withStyle(normalStyle)
        );
    }

    public static Palette styled(@NonNull ChatFormatting highlightStyle, @NonNull ChatFormatting normalStyle) {
        return new Palette(
                component -> component.withStyle(highlightStyle),
                component -> component.withStyle(normalStyle)
        );
    }

    public static Palette colored(@NonNull Color highlightColor, @NonNull Color normalColor) {
        return colored(highlightColor.rgb(), normalColor.rgb());
    }

    public static Palette colored(int highlightColor, int normalColor) {
        return new Palette(
                component -> component.withColor(highlightColor),
                component -> component.withColor(normalColor)
        );
    }

    public @NonNull UnaryOperator<@NonNull MutableComponent> getStyle(boolean highlighted) {
        return highlighted ? this.highlightStyle() : this.normalStyle();
    }

    public @NonNull MutableComponent style(@NonNull MutableComponent component, boolean highlighted) {
        return this.getStyle(highlighted).apply(component);
    }
}