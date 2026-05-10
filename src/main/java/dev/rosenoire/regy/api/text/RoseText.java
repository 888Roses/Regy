package dev.rosenoire.regy.api.text;

import dev.rosenoire.regy.api.data.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class RoseText {
    private @NonNull MutableComponent component;

    // region create

    private RoseText(@NonNull Component component) {
        this.component = component.copy();
    }

    public static RoseText text(@NonNull Component component) {
        return new RoseText(component);
    }

    public static RoseText text(@NonNull String text) {
        return text(Component.literal(text));
    }

    // endregion

    // region style

    public RoseText styled(@NonNull ChatFormatting... chatFormatting) {
        this.component.withStyle(chatFormatting);
        return this;
    }

    public RoseText styled(@NonNull Style style) {
        this.component.withStyle(style);
        return this;
    }

    public RoseText font(@NonNull FontDescription font) {
        return this.styled(Style.EMPTY.withFont(font));
    }

    public RoseText color(int color) {
        this.component.withColor(color);
        return this;
    }

    public RoseText color(@NonNull Color color) {
        return this.color(color.rgb());
    }

    // endregion

    // region alter

    public RoseText add(@NonNull Component component) {
        this.component.append(component.copy());
        return this;
    }

    public RoseText add(@NonNull String text) {
        this.component.append(text);
        return this;
    }

    public RoseText prepend(@NonNull Component component) {
        this.component = component.copy().append(this.component);
        return this;
    }

    public RoseText prepend(@NonNull String text) {
        if (text.isEmpty()) {
            return this;
        }

        return this.prepend(Component.literal(text));
    }

    public RoseText indent(int indent) {
        if (indent <= 0) {
            return this;
        }

        return this.prepend(" ".repeat(indent));
    }

    // endregion

    // region use

    public @NonNull MutableComponent get() {
        return this.component.copy();
    }

    public RoseText consume(@NonNull Consumer<@NonNull Component> consumer) {
        consumer.accept(get());
        return this;
    }

    // endregion
}