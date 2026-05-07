package dev.rosenoire.regy.pipeline.registration.item.material;

import dev.rosenoire.regy.pipeline.registration.AbstractSimpleEntry;
import net.minecraft.world.item.ToolMaterial;
import org.jspecify.annotations.NonNull;

public class ToolMaterialEntry extends AbstractSimpleEntry<ToolMaterial> {
    public ToolMaterialEntry(@NonNull ToolMaterial value) {
        super(value);
    }
}
