package dev.rosenoire.regy.pipeline.datagen.provider.tag;

import dev.rosenoire.regy.pipeline.datagen.ProviderContext;
import dev.rosenoire.regy.pipeline.datagen.ProviderType;
import dev.rosenoire.regy.pipeline.registration.item.item.ItemEntry;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

public class ItemTagDatagenProvider extends FabricTagProvider.ItemTagProvider implements TagDatagenProvider<Item, ItemEntry<? extends Item>> {
    private final ProviderContext ctx;

    public ItemTagDatagenProvider(ProviderContext ctx) {
        super(ctx.dataOutput(), ctx.registryLookup());
        this.ctx = ctx;
    }

    @Override
    public ProviderType getType() {
        return ProviderType.ITEM_TAG;
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        generateTagsForEntry(ctx, wrapperLookup);
    }

    @Override
    public @NonNull TagAppender<Item, Item> valueLookupBuilder(@NonNull TagKey<Item> key) {
        return super.valueLookupBuilder(key);
    }

    @Override
    public ResourceKey<? extends Registry<Item>> getRegistry() {
        return Registries.ITEM;
    }

    @Override
    public ItemEntry<? extends Item> mapDatagenEntry(@NonNull Object object) {
        return  object instanceof ItemEntry<? extends Item> itemEntry ? itemEntry : null;
    }
}
