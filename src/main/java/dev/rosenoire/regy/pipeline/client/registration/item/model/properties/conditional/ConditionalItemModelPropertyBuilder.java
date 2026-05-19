package dev.rosenoire.regy.pipeline.client.registration.item.model.properties.conditional;

import com.mojang.serialization.MapCodec;
import dev.rosenoire.regy.pipeline.client.AbstractClientRegy;
import dev.rosenoire.regy.pipeline.client.registration.item.model.properties.AbstractItemModelPropertyBuilder;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class ConditionalItemModelPropertyBuilder extends AbstractItemModelPropertyBuilder<
        ConditionalItemModelProperty,
        ConditionalItemModelPropertyEntry,
        MapCodec<? extends ConditionalItemModelProperty>,
        ConditionalItemModelPropertyBuilder
        > {
    public ConditionalItemModelPropertyBuilder(@NonNull AbstractClientRegy<?, ?> client, String identifier) {
        super(client, identifier);
    }

    @Override
    protected void registerProperty(Identifier identifier, MapCodec<? extends ConditionalItemModelProperty> codec) {
        ConditionalItemModelProperties.ID_MAPPER.put(identifier, codec);
    }

    @Override
    protected ConditionalItemModelPropertyEntry createEntry(Identifier identifier) {
        return new ConditionalItemModelPropertyEntry(identifier);
    }
}
