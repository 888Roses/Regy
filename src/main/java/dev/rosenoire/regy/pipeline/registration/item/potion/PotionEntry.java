package dev.rosenoire.regy.pipeline.registration.item.potion;

import dev.rosenoire.regy.pipeline.registration.AbstractSimpleEntry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.alchemy.Potion;
import org.jspecify.annotations.NonNull;

public class PotionEntry extends AbstractSimpleEntry<Potion> {
    public PotionEntry(@NonNull Potion value, Identifier regyIdentifier) {
        super(value, regyIdentifier);
    }
}
