package dev.rosenoire.regy.client.model.item.properties.select;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.rosenoire.regy.common.index.InternalDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record ItemSkinProperty() implements SelectItemModelProperty<String> {
    public static final SelectItemModelProperty.Type<ItemSkinProperty, String> TYPE = SelectItemModelProperty.Type.create(
            MapCodec.unit(new ItemSkinProperty()), Codec.STRING
    );

    @Override
    public @NonNull Codec<String> valueCodec() {
        return Codec.STRING;
    }

    @Override
    public @NonNull Type<? extends SelectItemModelProperty<String>, String> type() {
        return TYPE;
    }

    @Override
    public @Nullable String get(
            @NonNull ItemStack itemStack,
            @Nullable ClientLevel clientLevel,
            @Nullable LivingEntity livingEntity,
            int i,
            @NonNull ItemDisplayContext itemDisplayContext
    ) {
        var component = InternalDataComponents.ITEM_SKIN.get(itemStack);

        if (component == null) {
            return null;
        }

        return component.skins().get(component.index());
    }
}
