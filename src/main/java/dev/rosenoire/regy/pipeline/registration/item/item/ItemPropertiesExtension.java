package dev.rosenoire.regy.pipeline.registration.item.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public interface ItemPropertiesExtension {
    boolean regy$isLocked();

    String regy$getEffectiveDescriptionId();

    void regy$setLocked(boolean isLocked);

    default Item.Properties forceSetAttributes(ItemAttributeModifiers itemAttributeModifiers) {
        regy$setLocked(false);

        if (this instanceof Item.Properties properties) {
            properties.attributes(itemAttributeModifiers);
            return properties;
        }

        throw new IllegalCallerException("Cannot call 'forceSetAttributes' on an ItemPropertiesExtension that does not inherit from Item.Properties!");
    }
}
