package dev.rosenoire.regy.mixin;

import dev.rosenoire.regy.foundation.extensions.StringSplitterExtension;
import net.minecraft.client.StringSplitter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StringSplitter.class)
public class StringSplitterMixin implements StringSplitterExtension {
    @Shadow
    @Final
    StringSplitter.WidthProvider widthProvider;

    @Override
    public StringSplitter.WidthProvider regy$widthProvider() {
        return this.widthProvider;
    }
}
