package dev.rosenoire.regy.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemPropertiesExtension;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Item.Properties.class)
public abstract class ItemPropertiesMixin implements ItemPropertiesExtension {
    @Shadow protected abstract String effectiveDescriptionId();

    @Unique
    private boolean regy$isLocked;

    @Override
    public boolean regy$isLocked() {
        return regy$isLocked;
    }

    @Override
    public String regy$getEffectiveDescriptionId() {
        return effectiveDescriptionId();
    }

    @Override
    public void regy$setLocked(boolean isLocked) {
        this.regy$isLocked = isLocked;
    }

    @WrapMethod(method = "attributes")
    private Item.Properties attributes$respectLockedState(ItemAttributeModifiers itemAttributeModifiers, Operation<Item.Properties> original) {
        if (regy$isLocked) {
            return (Item.Properties) (Object) this;
        }

        var value = original.call(itemAttributeModifiers);
        regy$setLocked(true);
        return value;
    }
}
