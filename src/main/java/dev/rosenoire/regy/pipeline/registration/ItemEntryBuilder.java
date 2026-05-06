package dev.rosenoire.regy.pipeline.registration;

import dev.rosenoire.regy.api.text.NamingConventions;
import dev.rosenoire.regy.pipeline.AbstractRegy;
import dev.rosenoire.regy.pipeline.datagen.filter.DatagenTranslatable;
import dev.rosenoire.regy.pipeline.factory.ItemFactory;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@SuppressWarnings("UnusedReturnValue")
public class ItemEntryBuilder<I extends Item, P> extends AbstractEntryBuilder<ItemEntry<I>, P> implements DatagenTranslatable {
    private final ItemFactory<I> factory;
    private final ResourceKey<Item> resourceKey;
    private @Nullable String generatedName;

    public ItemEntryBuilder(@NonNull AbstractRegy<?> owner, P parent, String identifier, ItemFactory<I> factory) {
        super(owner, parent, identifier);
        this.factory = factory;
        this.resourceKey = ResourceKey.create(Registries.ITEM, identifier());

        this.simpleName();
    }

    public ItemEntryBuilder<I, P> name(@NotNull String name) {
        this.generatedName = name;
        return this;
    }

    public ItemEntryBuilder<I, P> simpleName() {
        return name(NamingConventions.HUMAN_TEXT.transform(path()));
    }

    @Override
    public ItemEntry<I> register() {
        var properties = new Item.Properties().setId(resourceKey);
        var item = factory.bake(properties);
        return getOwner().process(this, new ItemEntry<>(Registry.register(BuiltInRegistries.ITEM, resourceKey, item), resourceKey));
    }

    @Override
    public Translation getDatagenTranslation() {
        return new Translation(resourceKey.registry().getPath() + "." + getOwner().modNamespace() + "." + path(), this.generatedName);
    }
}