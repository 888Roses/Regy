package dev.rosenoire.regy.api.text;

import com.google.common.util.concurrent.AtomicDouble;
import dev.rosenoire.regy.api.data.Color;
import dev.rosenoire.regy.foundation.extensions.StringSplitterExtension;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class RoseText {
    private final List<@NonNull MutableComponent> lines;

    // region create

    public RoseText(@NonNull List<@NonNull MutableComponent> lines) {
        this.lines = lines;
    }

    public static RoseText text(@NonNull Component component) {
        return new RoseText(new ArrayList<>() {{
            add(component.copy());
        }});
    }

    public static RoseText text(@NonNull String text) {
        return text(Component.literal(text));
    }

    public static RoseText trans(@NonNull String key, Object... objects) {
        return text(Component.translatable(key, objects));
    }

    public static RoseText empty() {
        return text(Component.empty());
    }

    // endregion

    // region style

    public RoseText styled(@NonNull ChatFormatting... chatFormatting) {
        for (var component : this.lines) {
            component.withStyle(chatFormatting);
        }

        return this;
    }

    public RoseText styled(@NonNull Style style) {
        for (var component : this.lines) {
            component.withStyle(style);
        }

        return this;
    }

    public RoseText styled(@NonNull Palette palette, boolean highlighted) {
        return this.transform(palette.getStyle(highlighted));
    }

    public RoseText highlighted(@NonNull Palette palette) {
        return this.styled(palette, true);
    }

    public RoseText normal(@NonNull Palette palette) {
        return this.styled(palette, false);
    }

    public RoseText font(@NonNull FontDescription font) {
        return this.styled(Style.EMPTY.withFont(font));
    }

    public RoseText color(int color) {
        for (var component : this.lines) {
            component.withColor(color);
        }

        return this;
    }

    public RoseText color(@NonNull Color color) {
        return this.color(color.rgb());
    }

    // endregion

    // region alter

    public RoseText appendLast(@NonNull Component component) {
        if (!this.lines.isEmpty()) {
            this.lines.getLast().append(component.copy());
        } else {
            this.lines.add(component.copy());
        }

        return this;
    }

    public RoseText appendLast(@NonNull String text) {
        if (!this.lines.isEmpty()) {
            this.lines.getLast().append(text);
        } else {
            this.lines.add(Component.literal(text));
        }

        return this;
    }

    public RoseText appendAll(@NonNull Component component) {
        if (this.lines.isEmpty()) {
            return this;
        }

        return this.transform(line -> line.append(component));
    }

    public RoseText appendAll(@NonNull String text) {
        if (this.lines.isEmpty()) {
            return this;
        }

        return this.transform(line -> line.append(text));
    }

    public RoseText prependLast(@NonNull Component component) {
        if (!this.lines.isEmpty()) {
            this.lines.set(this.lines.size() - 1, component.copy().append(this.lines.getLast()));
        } else {
            this.lines.add(component.copy());
        }

        return this;
    }

    public RoseText prependLast(@NonNull String text) {
        if (text.isEmpty()) {
            return this;
        }

        return this.prependLast(Component.literal(text));
    }

    public RoseText prependAll(@NonNull Component component) {
        if (this.lines.isEmpty()) {
            return this;
        }

        return this.transform(line -> component.copy().append(line));
    }

    public RoseText prependAll(@NonNull String text) {
        if (text.isEmpty()) {
            return this;
        }

        return this.prependAll(Component.literal(text));
    }

    public RoseText indent(int indent) {
        return this.internal_genericIndent(this::prependAll, indent);
    }

    public RoseText indentLast(int indent) {
        return this.internal_genericIndent(this::prependLast, indent);
    }

    private RoseText internal_genericIndent(Function<String, RoseText> method, int indent) {
        if (indent <= 0) {
            return this;
        }

        return method.apply(" ".repeat(indent));
    }

    public RoseText transform(@NonNull UnaryOperator<@NonNull MutableComponent> operator) {
        this.lines.replaceAll(operator);
        return this;
    }

    public RoseText palette(@NonNull Palette palette) {
        return this.transform(component -> {
            var output = Component.empty();
            var currentSegment = new StringBuilder();
            var isHighlighted = false;

            for (var character : component.getString().toCharArray()) {
                if (character == '_') {
                    RoseText
                            .text(currentSegment.toString())
                            .styled(palette, isHighlighted)
                            .lastLine()
                            .ifPresent(output::append);

                    currentSegment = new StringBuilder();
                    isHighlighted = !isHighlighted;
                    continue;
                }

                currentSegment.append(character);
            }

            RoseText
                    .text(currentSegment.toString())
                    .styled(palette, isHighlighted)
                    .lastLine()
                    .ifPresent(output::append);

            return output;
        });
    }

    // Fixme: Is it fine if we use client sided features in the RoseText class?
    public RoseText wrap(int maxWidth) {
        var newLines = new ArrayList<MutableComponent>();
        var minecraft = Minecraft.getInstance();
        var splitter = minecraft.font.getSplitter();
        var widthProvider = ((StringSplitterExtension) splitter).regy$widthProvider();

        for (var line : this.lines) {
            var content = line.getString();
            var cumulatedWidth = new AtomicDouble();
            var component = new AtomicReference<>(Component.empty());

            line.getVisualOrderText().accept((index, style, codepoint) -> {
                var width = widthProvider.getWidth(codepoint, style);
                var current = cumulatedWidth.get();
                var next = current + width;

                component.get().append(Component
                        .literal(Character.toString(codepoint))
                        .setStyle(style));

                if (next >= maxWidth && Character.isWhitespace(codepoint)) {
                    next = 0;
                    newLines.add(component.get());
                    component.set(Component.empty());
                }

                cumulatedWidth.set(next);
                return true;
            });

            if (cumulatedWidth.get() > 0) {
                newLines.add(component.get());
            }
        }

        return new RoseText(newLines);
    }

    // endregion

    // region use

    public @NonNull List<MutableComponent> get() {
        return this.lines
                .stream()
                .map(Component::copy)
                .toList();
    }

    public Optional<MutableComponent> lastLine() {
        if (this.lines.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(this.lines.getLast().copy());
    }

    public Optional<MutableComponent> firstLine() {
        if (this.lines.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(this.lines.getFirst().copy());
    }

    public Optional<MutableComponent> line(int index) {
        if (this.lines.isEmpty() || (index < 0 || index >= this.lines.size() - 1)) {
            return Optional.empty();
        }

        return Optional.of(this.lines.get(index).copy());
    }

    public RoseText consume(@NonNull Consumer<@NonNull Component> consumer) {
        this.get().forEach(consumer);
        return this;
    }

    public RoseText forTooltip(@NonNull List<Component> tooltip) {
        return this.consume(tooltip::add);
    }

    public RoseText sendTo(@NonNull Player player, boolean aboveHotbar) {
        this.get().forEach(component -> player.displayClientMessage(component, aboveHotbar));
        return this;
    }

    public RoseText sendTo(@NonNull Player player) {
        return sendTo(player, false);
    }

    // endregion
}