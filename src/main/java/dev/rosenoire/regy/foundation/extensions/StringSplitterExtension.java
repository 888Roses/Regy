package dev.rosenoire.regy.foundation.extensions;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.StringSplitter;

@Environment(EnvType.CLIENT)
public interface StringSplitterExtension {
    StringSplitter.WidthProvider regy$widthProvider();
}
