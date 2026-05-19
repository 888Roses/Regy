package dev.rosenoire.regy.test.common.index;

import dev.rosenoire.regy.pipeline.registration.item.material.MaterialEntry;
import net.minecraft.world.item.ToolMaterial;

import java.awt.image.RGBImageFilter;

import static dev.rosenoire.regy.test.common.TestModCommon.REGY;

public interface AllMaterials {
    MaterialEntry ELDEN = REGY
            .material("elden")
            .copy(ToolMaterial.NETHERITE)
            .register();

    MaterialEntry AMARITE = REGY
            .material("amarite")
            .copy(ToolMaterial.NETHERITE)
            .register();

    static void register() {
    }
}
