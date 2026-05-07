package dev.rosenoire.regy.pipeline.registration.item.material;

import dev.rosenoire.regy.pipeline.registration.AbstractSimpleEntry;
import net.minecraft.world.item.ToolMaterial;
import org.jspecify.annotations.NonNull;

public class MaterialEntry extends AbstractSimpleEntry<ToolMaterial> {
    public MaterialEntry(@NonNull ToolMaterial value) {
        super(value);
    }
}
