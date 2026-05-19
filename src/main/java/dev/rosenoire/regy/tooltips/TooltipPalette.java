package dev.rosenoire.regy.tooltips;

import dev.rosenoire.regy.api.data.Colors;
import dev.rosenoire.regy.api.text.Palette;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.RegyInternal;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public record TooltipPalette(
        Palette headerFolded,
        Palette headerUnfolded,
        Palette body,
        Palette condition,
        Palette behaviour
) {
    public static final TooltipPalette DEFAULT = builder().build();

    public static Builder builder() {
        return new Builder();
    }

    public Palette getHeaderPalette(boolean unfolded) {
        return unfolded ? this.headerUnfolded() : this.headerFolded();
    }

    public static TooltipPalette getPalette(Item item) {
        return RegyInternal.regyFromItem(item)
                .map(AbstractRegy::tooltipPalette)
                .orElse(DEFAULT);
    }

    public static final class Builder {
        private Palette headerFolded = Palette.colored(Colors.GRAY, Colors.DARK_GRAY);
        private Palette headerUnfolded = Palette.colored(Colors.WHITE, Colors.DARK_GRAY);
        private Palette body = Palette.colored(Colors.GRAY, Colors.DARK_GRAY);
        private Palette condition = Palette.colored(Colors.WHITE, Colors.WHITE);
        private Palette behaviour = Palette.colored(Colors.GRAY, Colors.DARK_GRAY);

        private Builder() {
        }

        public Builder headerFolded(Palette headerFolded) {
            this.headerFolded = headerFolded;
            return this;
        }

        public Builder headerUnfolded(Palette headerUnfolded) {
            this.headerUnfolded = headerUnfolded;
            return this;
        }

        public Builder body(Palette body) {
            this.body = body;
            return this;
        }

        public Builder condition(Palette condition) {
            this.condition = condition;
            return this;
        }

        public Builder behaviour(Palette behaviour) {
            this.behaviour = behaviour;
            return this;
        }

        public TooltipPalette build() {
            return new TooltipPalette(
                    this.headerFolded,
                    this.headerUnfolded,
                    this.body,
                    this.condition,
                    this.behaviour
            );
        }
    }
}