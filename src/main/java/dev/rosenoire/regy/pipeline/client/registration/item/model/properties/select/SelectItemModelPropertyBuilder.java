package dev.rosenoire.regy.pipeline.client.registration.item.model.properties.select;

import dev.rosenoire.regy.pipeline.client.AbstractClientRegy;
import dev.rosenoire.regy.pipeline.client.registration.item.model.properties.AbstractItemModelPropertyBuilder;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class SelectItemModelPropertyBuilder extends AbstractItemModelPropertyBuilder<
        ConditionalItemModelProperty,
        SelectItemModelPropertyEntry,
        SelectItemModelProperty.Type<?, ?>,
        SelectItemModelPropertyBuilder
        > {
    public SelectItemModelPropertyBuilder(@NonNull AbstractClientRegy<?, ?> client, String identifier) {
        super(client, identifier);
    }

    @Override
    protected void registerProperty(Identifier identifier, SelectItemModelProperty.Type<?, ?> type) {
        SelectItemModelProperties.ID_MAPPER.put(identifier, type);
    }

    @Override
    protected SelectItemModelPropertyEntry createEntry(Identifier identifier) {
        return new SelectItemModelPropertyEntry(identifier);
    }
}
